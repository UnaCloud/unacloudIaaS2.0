package virtualMachineManager;

import static com.losandes.utils.Constants.ERROR_MESSAGE;
import hypervisorManager.Hypervisor;
import hypervisorManager.HypervisorFactory;
import hypervisorManager.HypervisorOperationException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;

import monitoring.VirtualMachineStateViewer;
import unacloudEnums.VirtualMachineExecutionStateEnum;
import communication.ServerMessageSender;
import communication.UnaCloudAbstractResponse;
import communication.messages.vmo.VirtualMachineAddTimeMessage;
import communication.messages.vmo.VirtualMachineRestartMessage;

/**
 * Responsible for managing virtual machine executions. This class is responsible to schedule virtual machine startups and
 * stops. The process is: given a virtual machine and a  time t, this class ensures that this virtual machine is going to be turn on for a time t
 * The virtual machine only is stopped when the time t is burnt-out or when the user sends a request to stop it. If this physical machine is turned off,
 * then the next time the physical machine starts the virtual machine will be powered on.<br/>
 * To schedule the virtual machine turn off we used a Timer that manage a collection of TimerTask objects, each timer task is responsible for
 * stopping one virtual machine
 * @author Clouder
 */
public class PersistentExecutionManager {

    /**
     * The file that contains the powered virtual machines and its execution times
     */
    private static final String executionsFile = "executions.txt";
    
    private static Map<Long,VirtualMachineExecution> executionList=new TreeMap<>();
    
    /**
     * Timer used to schedule shutdown events
     */
    private static Timer timer = new Timer();
    
        /**
     * Stops a virtual machine and removes it representing execution object
     * @param turnOffMEssage
     * @return
     */
    public static UnaCloudAbstractResponse removeExecution(long virtualMachineExecutionId,boolean checkTime) {
    	VirtualMachineExecution execution=null;
    	execution=executionList.remove(virtualMachineExecutionId);
		if(execution!=null&&(!checkTime||System.currentTimeMillis()>execution.getShutdownTime())){
			Hypervisor v=HypervisorFactory.getHypervisor(execution.getImage().getHypervisorId());
			v.stopVirtualMachine(execution.getImage());
			v.unregisterVirtualMachine(execution.getImage());
		}
		saveData();
    	return null;
    }

    /**
     * Restarts the given virtual machine
     * @param hypervisorName The hypervisor that must be used to stop this virtual machine
     * @param vmPath The path of the virtual machine to be stoped
     * @param hypervisorPath The path of the hypervisor exec that must be used to stop the machine
     * @param id The id of the virtual machine execution to be removed     *
     * @return
     */
    public static UnaCloudAbstractResponse restartMachine(VirtualMachineRestartMessage restartMessage) {
    	VirtualMachineExecution execution=executionList.get(restartMessage.getVirtualMachineExecutionId());
        Hypervisor v=HypervisorFactory.getHypervisor(execution.getImage().getHypervisorId());
        try {
            v.restartVirtualMachine(execution.getImage());
        } catch (HypervisorOperationException ex) {
            ServerMessageSender.reportVirtualMachineState(restartMessage.getVirtualMachineExecutionId(), VirtualMachineExecutionStateEnum.FAILED, ex.getMessage());
        }
        saveData();
        return null;
    }

    /**
     * Starts and configures a virtual machine. this method must be used by other methods to configure, start and schedule a virtual machine execution
     * @param turnOnMessage
     * @return
     */
    public static String startUpMachine(VirtualMachineExecution execution){
    	execution.setShutdownTime(System.currentTimeMillis()+execution.getExecutionTime()*3600000);
        Hypervisor v=HypervisorFactory.getHypervisor(execution.getImage().getHypervisorId());
        try {
            v.startVirtualMachine(execution.getImage());
            executionList.put(execution.getId(),execution);
            timer.schedule(new Schedule(execution.getId()),new Date(execution.getShutdownTime()+100));
            new VirtualMachineStateViewer(execution.getId(),v,execution.getIp());
        } catch (HypervisorOperationException e) {
        	HypervisorFactory.getHypervisor(execution.getImage().getHypervisorId()).stopVirtualMachine(execution.getImage());
        	ServerMessageSender.reportVirtualMachineState(execution.getId(), VirtualMachineExecutionStateEnum.FAILED, e.getMessage());
            return ERROR_MESSAGE + e.getMessage();
        }
        saveData();
        return "";
    }
    
    /**
     * Extends the time that the virtual machine must be up
     * @param id The execution id to find the correspondig virtual machine
     * @param executionTime The aditional time that must be added to the virtual machine execution
     */
    public static UnaCloudAbstractResponse extendsVMTime(VirtualMachineAddTimeMessage timeMessage) {
    	VirtualMachineExecution execution=executionList.get(timeMessage.getVirtualMachineExecutionId());
    	execution.setExecutionTime(timeMessage.getExecutionTime());
    	execution.setShutdownTime(System.currentTimeMillis()+timeMessage.getExecutionTime()*3600000);
    	timer.schedule(new Schedule(execution.getId()),new Date(execution.getShutdownTime()+100));
    	saveData();
        return null;
    }
    
    /**
     * Saves the current state of virtual machine executions and virtual machine images on this node.  
     */
    private static void saveData(){
    	try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(executionsFile));){
        	oos.writeObject(executionList);
        	oos.writeObject(VirtualMachineImageManager.imageList);
        }catch(Exception e){}
    }
    /**
     * Loads the stored virtual machine executions when the physical machine start.
     * Each loaded execution is used to turn on the corresponding virtual machine
     */
    @SuppressWarnings("unchecked")
	public static void loadData(){
    	new Thread(){
    		public void run() {
    			Map<Long,VirtualMachineExecution> executions=null;
    	    	List<VirtualMachineImage> images=null;
    	        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(executionsFile))){
    	        	executions=(Map<Long,VirtualMachineExecution>)ois.readObject();
    	        	images=(List<VirtualMachineImage>)ois.readObject();
    	        	if(executions!=null&&images!=null){
    	            	executionList=executions;
    	            	VirtualMachineImageManager.imageList=images;
    	            }else saveData();
    	        } catch (Exception ex){}
    		};
    	}.start();
    }
}
