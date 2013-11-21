/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package execution;

import communication.ServerMessageSender;
import communication.messages.vmo.VirtualMachineAddTimeMessage;
import communication.messages.vmo.VirtualMachineRestartMessage;
import communication.messages.vmo.VirtualMachineTurnOffMessage;
import communication.messages.vmo.VirtualMachineTurnOnMessage;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;

import monitoring.VirtualMachineStateViewer;

import com.losandes.utils.Log;

import physicalmachine.MachineMonitor;
import virtualmachine.Hypervisor;
import virtualmachine.HypervisorFactory;
import static com.losandes.utils.Constants.*;
import virtualmachine.HypervisorOperationException;

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

    /**
     * A map containing a map between virtual machine ids and its scheduled TimerTask to stop it.
     */
    private static Map<String,Schedule> programedShutdowns = new TreeMap<String,Schedule>();

    /**
     * Timer used to schedule shutdown events
     */
    private static Timer timer = new Timer();

    /**
     * Loads the stored virtual machine executions when the physical machine start.
     * Each loaded execution used to turn on the corresponding virtual machine
     */
    public static void loadExecutions() {
        try {
            long l = System.currentTimeMillis() + 60000l;
            ArrayList<String> execs = new ArrayList<String>();
            for (String h:getExecutions()) {
                String[] j = h.split(";");
                String vmRoute = j[0];
                String exId = j[1];
                long endTime = Long.parseLong(j[2]);
                String vmrunPath = j[3];
                String vmIp = j[4];
                String checkPoint = j[5];
                if (endTime > l) {
                    execs.add(h);
                    startUpMachine(vmRoute, vmrunPath, vmIp, exId, endTime-l, VMW,Integer.parseInt(checkPoint));
                    ServerMessageSender.reportVirtualMachineState(exId,ON_STATE,"Machine started");
                }
            }
            setExecutions(execs);
        } catch (Exception ex){}
    }

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
        try{
            PrintWriter pw = new PrintWriter(new FileOutputStream(executionsFile,true));
            pw.println(turnOnMessage.getVmPath()+";"+turnOnMessage.getVirtualMachineExecutionId()+";"+(System.currentTimeMillis()+turnOnMessage.getExecutionTime()*3600000)+";"+turnOnMessage.getHypervisorPath()+";"+turnOnMessage.getVmIP()+";"+turnOnMessage.getCheckPoint());
            pw.close();
        }catch(Exception e){}
        startUpMachine(turnOnMessage);
    }

    /**
     * Stops a virtual machine and removes it represeting execution object
     * @param hypervisorName The hypervisor that must be used to stop this virtual machine
     * @param vmPath The path of the virtual machine to be stoped
     * @param hypervisorPath The path of the hypervisor exec that must be used to stop the machine
     * @param id The id of the virtual machine execution to be removed
     * @return
     */
    public static String removeExecution(VirtualMachineTurnOffMessage turnOffMEssage) {
        ArrayList<String> execs=new ArrayList<String>();
        for(String h:getExecutions())if(!h.split(";")[1].equals(turnOffMEssage.getVirtualMachineExecutionId()))execs.add(h);
        setExecutions(execs);
        Hypervisor v=HypervisorFactory.getHypervisor(turnOffMEssage.getHypervisorName(),turnOffMEssage.getHypervisorPath(),turnOffMEssage.getVmPath());
        try{
            programedShutdowns.remove(turnOffMEssage.getVirtualMachineExecutionId()).cancel();
        }catch(Exception e){}
        try {
            v.turnOffVirtualMachine();
            return "";
        } catch (HypervisorOperationException e) {
            return ERROR_MESSAGE + e.getMessage();
        }
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
     * Starts and configures a virtual machine. this methos must be used by other methods to configure, start and schedule a virtual machine execution
     * @param vmxroute The route of the virtual machine to be managed
     * @param hypervisorPath The hypervisor that must be used to start and stop the given virtual machine
     * @param vmIP The ip of the virtual machine that is being deployed
     * @param id The id of the new managed virtual machine execution
     * @param executionTime The time that the machine must stay turned on
     * @param vmCores The number of cores that must be configured on the virtual machine to deploy it
     * @param vmMemory The RAM memory size that must be configured on the virtual machine to deploy it
     * @param hypervisorName The type of hypervisor that must be used to deploy this virtual machine
     * @param persistent If the virtual machine must persist its files after machine stop or if it must rollback to its initial status
     * @return
     */
    private static String startUpMachine(VirtualMachineTurnOnMessage turnOnMessage) {
        Hypervisor v=HypervisorFactory.getHypervisor(turnOnMessage.getHypervisorName(),turnOnMessage.getHypervisorPath(),turnOnMessage.getVmPath());
        try {
            v.preconfigureAndStartVirtualMachine(turnOnMessage.getVmCores(),turnOnMessage.getVmMemory(),turnOnMessage.getPersistent());
            Schedule timeExec = new Schedule(turnOnMessage.getHypervisorName(),turnOnMessage.getHypervisorPath(),VMW_TURN_OFF,turnOnMessage.getVmPath());
            programedShutdowns.put(turnOnMessage.getVirtualMachineExecutionId(),timeExec);
            timer.schedule(timeExec,turnOnMessage.getExecutionTime());
            new VirtualMachineStateViewer(turnOnMessage.getVirtualMachineExecutionId(),v,turnOnMessage.getVmIP());
            MachineMonitor.addMachineExecution(turnOnMessage.getVirtualMachineExecutionId(),turnOnMessage.getVmPath(),turnOnMessage.getVmCores());
        } catch (HypervisorOperationException e) {
            Log.print("Error al levantar la máquina " + e.getMessage());
            removeExecution(turnOnMessage.getHypervisorName(), turnOnMessage.getVmPath(), turnOnMessage.getHypervisorPath(),turnOnMessage.getVirtualMachineExecutionId());
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
    private static String startUpMachine(String vmxroute, String hypervisorPath, String vmIP, String id, long executionTimeMilis, int hypervisorName,int checkPoint) {
        if(checkPoint==1)try {
            LocalProcessExecutor.executeCommand("\"" + hypervisorPath + "\\vmrun\" revertToSnapshot \""+vmxroute+"\" \"AutoProtect Snapshot/AutoProtect Snapshot/AutoProtect Snapshot/AutoProtect Snapshot/AutoProtect Snapshot\"");
        } catch (Exception ex) {
            Log.print(ex.getLocalizedMessage());
        }
        return startUpMachine(vmxroute, hypervisorPath, vmIP, id, executionTimeMilis, 0,0,hypervisorName, "Not used");
    }

    /**
     * Extends the time that the virtual machine must be up
     * @param id The execution id to find the correspondig virtual machine
     * @param executionTime The aditional time that must be added to the virtual machine execution
     */
    public static void extendsVMTime(VirtualMachineAddTimeMessage timeMessage) {
        Log.print("extendsVMTime "+timeMessage.getVirtualMachineExecutionId()+" "+timeMessage.getExecutionTime());
        Schedule timeExec=programedShutdowns.remove(timeMessage.getVirtualMachineExecutionId());
        int hypervisorName = timeExec.getHypervisorName();
        String hypervisorPath = timeExec.getHypervisorPath();
        String virtualMachinePath = timeExec.getVirtualMachinePath();
        timeExec.cancel();
        timeExec = new Schedule(hypervisorName, hypervisorPath, VMW_TURN_OFF, virtualMachinePath);
        programedShutdowns.put(timeMessage.getVirtualMachineExecutionId(), timeExec);
        timer.schedule(timeExec, timeMessage.getExecutionTime());
        Log.print("extendedVMTime");
    }

    /**
     * Restores the current managed virtual machine executions from the persitant file
     * @return
     */
    private static String[] getExecutions(){
        ArrayList<String> temp = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(executionsFile));
            for(String h;(h=br.readLine())!=null;)temp.add(h);
            br.close();
        }catch(Exception e){}
        return temp.toArray(new String[0]);
    }

    /**
     * Persists the given virtual machine executions
     * @param executions
     */
    private static void setExecutions(ArrayList<String> executions){
        PrintWriter pw=null;
        try{
            pw = new PrintWriter(executionsFile);
            for(String h:executions)pw.println(h);
            pw.close();
        }catch(Exception e){
            if(pw!=null)pw.close();
        }
    }
}
