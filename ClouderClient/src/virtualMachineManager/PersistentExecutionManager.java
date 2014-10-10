package virtualMachineManager;

import static com.losandes.utils.Constants.ERROR_MESSAGE;
import hypervisorManager.HypervisorOperationException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
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
    
    private static final Map<Long,VirtualMachineExecution> executionList=new TreeMap<>();
    
    /**
     * Timer used to schedule shutdown events
     */
    private static Timer timer = new Timer();
    
        /**
     * Stops a virtual machine and removes it representing execution object
     * @param turnOffMEssage
     * @return
     */
    public static void removeExecution(long virtualMachineExecutionId,boolean checkTime) {
    	VirtualMachineExecution execution=executionList.remove(virtualMachineExecutionId);
		if(execution!=null&&(!checkTime||System.currentTimeMillis()>execution.getShutdownTime())){
			execution.getImage().stopAndUnregister();
		}
		saveData();
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
        try {
        	execution.getImage().restartVirtualMachine();
        } catch (HypervisorOperationException ex) {
            ServerMessageSender.reportVirtualMachineState(restartMessage.getVirtualMachineExecutionId(), VirtualMachineExecutionStateEnum.FAILED, ex.getMessage());
        }
        saveData();
        return null;
    }

    /**
     * Starts and configures a virtual machine. this method must be used by other methods to configure, start and schedule a virtual machine execution
     * @param turnOnMessage
     * @param started Parameter that tell if the VM is already on and should not be started again. 
     * @return
     */
    public static String startUpMachine(VirtualMachineExecution execution,boolean started){
    	execution.setShutdownTime(System.currentTimeMillis()+execution.getExecutionTime().toMillis());
        try {
            if(!started)execution.getImage().startVirtualMachine();
            executionList.put(execution.getId(),execution);
            timer.schedule(new Schedule(execution.getId()),new Date(execution.getShutdownTime()+100l));
            new VirtualMachineStateViewer(execution.getId(),execution.getIp());
        } catch (HypervisorOperationException e) {
        	e.printStackTrace();
        	execution.getImage().stopAndUnregister();
        	ServerMessageSender.reportVirtualMachineState(execution.getId(), VirtualMachineExecutionStateEnum.FAILED, e.getMessage());
            return ERROR_MESSAGE + e.getMessage();
        }
        saveData();
        return "";
    }
    
    /**
     * Extends the time that the virtual machine must be up
     * @param id The execution id to find the corresponding virtual machine
     * @param executionTime The additional time that must be added to the virtual machine execution
     */
    public static UnaCloudAbstractResponse extendsVMTime(VirtualMachineAddTimeMessage timeMessage) {
    	VirtualMachineExecution execution=executionList.get(timeMessage.getVirtualMachineExecutionId());
    	execution.setExecutionTime(timeMessage.getExecutionTime());
    	execution.setShutdownTime(System.currentTimeMillis()+timeMessage.getExecutionTime().toMillis());
    	timer.schedule(new Schedule(execution.getId()),new Date(execution.getShutdownTime()+100l));
    	saveData();
        return null;
    }
    
    /**
     * Saves the current state of virtual machine executions and virtual machine images on this node.  
     */
    private static void saveData(){
    	try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(executionsFile));){
        	oos.writeObject(executionList);
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
    	    	try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(executionsFile))){
    	        	executions=(Map<Long,VirtualMachineExecution>)ois.readObject();
    	        	if(executions!=null){
    	        		for(VirtualMachineExecution execution:executions.values())if(execution!=null){
	        				execution.getImage().stopAndUnregister();
    	        		}
    	            }else saveData();
    	        } catch (Exception ex){}
    		};
    	}.start();
    }
}
