package execution;

import static com.losandes.utils.Constants.ERROR_MESSAGE;
import static com.losandes.utils.Constants.ERROR_STATE;
import static com.losandes.utils.Constants.VMW_TURN_OFF;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;

import monitoring.VirtualMachineStateViewer;
import physicalmachine.MachineMonitor;
import virtualmachine.Hypervisor;
import virtualmachine.HypervisorFactory;
import virtualmachine.HypervisorOperationException;

import communication.ServerMessageSender;
import communication.messages.vmo.VirtualMachineAddTimeMessage;
import communication.messages.vmo.VirtualMachineRestartMessage;
import communication.messages.vmo.VirtualMachineTurnOnMessage;

/**
 * Responsible for managing virtual machine executions. This class is responsible to schedule virtual machine startups and
 * stops. The process is: given a virtual machine and a  time t, this class ensures that this virtual machine is going to be turn on for a time t
 * The virtual machine only is stoped when the time t is burnt-out or when the user sends a request to stop it. If this physical machine is turned off,
 * then the next time the physical machine starts the virtual machine will be powered on.<br/>
 * To schedule the virtual machine turn off we used a Timer that manage a collection of TimerTask objects, each timer task is responsible for
 * stoping one virtual machine
 * @author Clouder
 */
public class PersistentExecutionManager {

    /**
     * The file that contains the powered virtual machines and its execution times
     */
    private static final String executionsFile = "executions.txt";
    
    private static Map<String,VirtualMachineTurnOnMessage> turnOnMachinesList=new TreeMap<>();

    /**
     * A map containing a map between virtual machine ids and its scheduled TimerTask to stop it.
     */
    private static Map<String,Schedule> programedShutdowns = new TreeMap<String,Schedule>();
    
    /**
     * Timer used to schedule shutdown events
     */
    private static Timer timer = new Timer();

    

    /**
     * Adds a new virtual machine execution to be managed by this class.
     * @param vmxroute The route of the virtual machine to be managed
     * @param hypervisorPath The hypervisor that must be used to start and stop the given virtual machine
     * @param vmIP The ip of the virtual machine that is being deployed
     * @param id The id of the new managed virtual machine execution
     * @param executionTime The time that the machine must stay turned on
     * @param vmCores The number of cores that must be configured on the virtual machine to deploy it
     * @param vmMemory The RAM memory size that must be configured on the virtual machine to deploy it
     * @param hypervisorName The type of hypervisor that must be used to deploy this virtual machine
     * @param persistent If the virtual machine must persist its files after machine stop or if it must rollback to its initial status
     * @param checkPoint The name of the checkpoint that must be taken to secure this virtual machine execution, null if no checkpoint is needed
     */
    public static void addExecution(VirtualMachineTurnOnMessage turnOnMessage) {
    	startUpMachine(turnOnMessage);
    }

    /**
     * Stops a virtual machine and removes it representing execution object
     * @param turnOffMEssage
     * @return
     */
    public static String removeExecution(String virtualMachineExecutionId) {
    	VirtualMachineTurnOnMessage turnOnMessage=null;
    	try{
    		turnOnMessage=turnOnMachinesList.remove(virtualMachineExecutionId);
            programedShutdowns.remove(virtualMachineExecutionId).cancel();
        }catch(Exception e){}
    	if(turnOnMessage!=null){
        	HypervisorFactory.getHypervisor(turnOnMessage.getHypervisorName(),turnOnMessage.getHypervisorPath(),turnOnMessage.getVmPath()).turnOffVirtualMachine();
            return "";
    	}
    	return "";
    }

    /**
     * Restarts the given virtual machine
     * @param hypervisorName The hypervisor that must be used to stop this virtual machine
     * @param vmPath The path of the virtual machine to be stoped
     * @param hypervisorPath The path of the hypervisor exec that must be used to stop the machine
     * @param id The id of the virtual machine execution to be removed     *
     * @return
     */
    public static String restartMachine(VirtualMachineRestartMessage restartMessage) {
        String result = "";
        Hypervisor v=HypervisorFactory.getHypervisor(restartMessage.getHypervisorName(),restartMessage.getHypervisorPath(),restartMessage.getVmPath());
        try {
            v.restartVirtualMachine();
        } catch (HypervisorOperationException ex) {
            ServerMessageSender.reportVirtualMachineState(restartMessage.getVirtualMachineExecutionId(), ERROR_STATE, ex.getMessage());
        }
        return result;
    }

    /**
     * Starts and configures a virtual machine. this method must be used by other methods to configure, start and schedule a virtual machine execution
     * @param turnOnMessage
     * @return
     */
    private static String startUpMachine(VirtualMachineTurnOnMessage turnOnMessage){
    	turnOnMessage.setShutdownTime(System.currentTimeMillis()+turnOnMessage.getExecutionTime()*3600000);
        Hypervisor v=HypervisorFactory.getHypervisor(turnOnMessage.getHypervisorName(),turnOnMessage.getHypervisorPath(),turnOnMessage.getVmPath());
        try {
            v.preconfigureAndStartVirtualMachine(turnOnMessage.getVmCores(),turnOnMessage.getVmMemory(),turnOnMessage.getPersistent());
            programShutdown(turnOnMessage);
            new VirtualMachineStateViewer(turnOnMessage.getVirtualMachineExecutionId(),v,turnOnMessage.getVmIP());
            MachineMonitor.addMachineExecution(turnOnMessage.getVirtualMachineExecutionId(),turnOnMessage.getVmPath(),turnOnMessage.getVmCores());
        } catch (HypervisorOperationException e) {
			HypervisorFactory.getHypervisor(turnOnMessage.getHypervisorName(),turnOnMessage.getHypervisorPath(),turnOnMessage.getVmPath()).turnOffVirtualMachine();
            ServerMessageSender.reportVirtualMachineState(turnOnMessage.getVirtualMachineExecutionId(), ERROR_STATE, e.getMessage());
            return ERROR_MESSAGE + e.getMessage();
        }
        return "";
    }

    /**
     * Starts a virtual machine after returning to its last snapshot
     * @param vmxroute The route of the virtual machine to be managed
     * @param hypervisorPath The hypervisor that must be used to start and stop the given virtual machine
     * @param vmIP The ip of the virtual machine that is being deployed
     * @param id The id of the new managed virtual machine execution
     * @param executionTime The time that the machine must stay turned on
     * @param hypervisorName The type of hypervisor that must be used to deploy this virtual machine
     * @param checkPoint
     * @return
     */
    /*private static String startUpMachine(String vmxroute, String hypervisorPath, String vmIP, String id, long executionTimeMilis, int hypervisorName,int checkPoint) {
        if(checkPoint==1)try {
            LocalProcessExecutor.executeCommand("\"" + hypervisorPath + "\\vmrun\" revertToSnapshot \""+vmxroute+"\" \"AutoProtect Snapshot/AutoProtect Snapshot/AutoProtect Snapshot/AutoProtect Snapshot/AutoProtect Snapshot\"");
        } catch (Exception ex) {
            Log.print(ex.getLocalizedMessage());
        }
        return startUpMachine(vmxroute, hypervisorPath, vmIP, id, executionTimeMilis, 0,0,hypervisorName, "Not used");
    }*/

    /**
     * Extends the time that the virtual machine must be up
     * @param id The execution id to find the correspondig virtual machine
     * @param executionTime The aditional time that must be added to the virtual machine execution
     */
    public static void extendsVMTime(VirtualMachineAddTimeMessage timeMessage) {
    	VirtualMachineTurnOnMessage turnOnMessage=turnOnMachinesList.get(timeMessage.getVirtualMachineExecutionId());
    	turnOnMessage.setExecutionTime(timeMessage.getExecutionTime());
        programedShutdowns.remove(timeMessage.getVirtualMachineExecutionId()).cancel();
        programShutdown(turnOnMessage);
        saveExecutions();
    }
    private static boolean programShutdown(VirtualMachineTurnOnMessage turnOnMessage){
    	Schedule timeExec = new Schedule(turnOnMessage.getHypervisorName(),turnOnMessage.getHypervisorPath(),VMW_TURN_OFF,turnOnMessage.getVmPath());
        programedShutdowns.put(turnOnMessage.getVirtualMachineExecutionId(),timeExec);
        timer.schedule(timeExec,new Date(turnOnMessage.getShutdownTime()));
        return true;
    }
    
    private static void saveExecutions(){
    	try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(executionsFile));){
        	oos.writeObject(turnOnMachinesList);
        }catch(Exception e){}
    }
    /**
     * Loads the stored virtual machine executions when the physical machine start.
     * Each loaded execution used to turn on the corresponding virtual machine
     */
    public static void loadExecutions(){
    	Map<String,VirtualMachineTurnOnMessage> lastShutdowns=null;
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(executionsFile))){
        	lastShutdowns=(Map<String,VirtualMachineTurnOnMessage>)ois.readObject();
        } catch (Exception ex){}
        if(lastShutdowns!=null){
        	
        }
    }
}
