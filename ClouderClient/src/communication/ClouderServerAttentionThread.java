package communication;

import communication.messages.UnaCloudAbstractMessage;
import communication.security.utils.AbstractCommunicator;
import configuration.VirtualMachineConfigurator;
import execution.PersistentExecutionManager;
import fileTransfer.FileTrasferAttender;
import fileTransfer.UnicastSender;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import monitoring.PhysicalMachineMonitor;
import physicalmachine.OperatingSystem;
import static com.losandes.utils.Constants.*;

/**
 * @author Eduardo Rosales Responsible for attending or discarding a Clouder
 * Server operation request in a thread
 */
public class ClouderServerAttentionThread extends Thread {

    /**
     * Abstract comunnicator used to recieve the request and send a response
     */
    private Socket communication;
    
    /**
     * Owner of this object. Used to attend upgrade and stop requests
     */
    private Closeable clouderAttention;

    /**
     * Constructs an attention thread for a given communicator channel. Recieves
     * an owner object for stop requests
     *
     * @param clientSocket
     * @param cca The owner of this object
     */
    public ClouderServerAttentionThread(Socket clientSocket, Closeable cca) {
        communication = clientSocket;
        clouderAttention = cca;
    }

    /**
     * Responsible for attending or discarding the Clouder Server operation
     * request
     */
    public void run() {
        try(ObjectInputStream ois=new ObjectInputStream(communication.getInputStream());PrintWriter pw=new PrintWriter(communication.getOutputStream())){
        	for(UnaCloudAbstractMessage clouderServerRequest;(clouderServerRequest = UnaCloudAbstractMessage.fromMessage((UnaCloudMessage)ois.readObject()))!=null;){
        		switch (clouderServerRequest.getMainOp()) {
	                case UnaCloudAbstractMessage.VIRTUAL_MACHINE_OPERATION:
	                    VIRTUAL_MACHINE_OPERATION(clouderServerRequest);
	                    break;
	                case UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION:
	                	//TODO do something
	                    //PHYSICAL_MACHINE_OPERATION(clouderServerRequest, communication);
	                    break;
	                case UnaCloudAbstractMessage.UPDATE_OPERATION:
	                    clouderAttention.close();
	                    try {
	                        Runtime.getRuntime().exec("javaw -jar ClientUpdater.jar 6");
	                    } catch (Exception e) {
	                    }
	                    System.exit(6);
	                    break;
	                case STOP_CLIENT:
	                    clouderAttention.close();
	                    System.exit(0);
	                    break;
	                case UnaCloudAbstractMessage.VIRTUAL_MACHINE_CONFIGURATION:
	                	//TODO do something
	                    //new VirtualMachineConfigurator().attendConfigurationRequest(clouderServerRequest, communication);
	                case GET_VERSION:
	                    pw.println("1.30");
	                default:
	                    //clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server request is null or an invalid number: " + operationDomain;
	                    //System.err.println(clouderClientOperationResult);
	                    break;
        		}
        	}
        } catch (Exception ex) {
            ex.printStackTrace();
            // System.err.println("The communication process with Clouder Server failed in ClouderServerAttentionThread: " + ex.getMessage());
        }
    }

    /**
     * Method responsible for attending requests for operations over virtual
     * machines
     *
     * @param clouderServerRequestSplitted Server request
     */
    private void VIRTUAL_MACHINE_OPERATION(UnaCloudAbstractMessage message) {
        //virtualOperationType = {VM_TURN_ON, VM_TURN_OFF, VM_RESTART, VM_STATE}
        int virtualOperationType = message.getSubOp();
        String virtualMachineCode = message.getString(2);
        if (virtualMachineCode == null || virtualOperationType == -1) {
            return;
        }
        switch (virtualOperationType) {
            case VM_TURN_ON:
                if (message.length > 11) {
                    int hypervisorName = message.getInteger(3);
                    int vmCores = message.getInteger(4);
                    int vmMemory = message.getInteger(5);
                    String vmPath = message.getString(6);
                    String hypervisorPath = message.getString(7);
                    int executionTime = message.getInteger(8);
                    String vmIP = message.getString(9);
                    String persistent = message.getString(10);
                    String checkPoint = message.getString(11);
                    String snapshotRoute = message.getString(12);
                    PersistentExecutionManager.addExecution(vmPath, hypervisorPath, vmIP, virtualMachineCode, executionTime, vmCores, vmMemory, hypervisorName, persistent, checkPoint);
                }
                break;
            case VM_TURN_OFF:
                if (message.length > 5) {
                    int hypervisorName = message.getInteger(3);
                    String vmPath = message.getString(4);
                    String hypervisorPath = message.getString(5);
                    PersistentExecutionManager.removeExecution(hypervisorName, vmPath, hypervisorPath, virtualMachineCode);
                }
                break;
            case VM_RESTART:
                if (message.length > 5) {
                    int hypervisorName = message.getInteger(3);
                    String vmPath = message.getString(4);
                    String hypervisorPath = message.getString(5);
                    PersistentExecutionManager.restartMachine(hypervisorName, vmPath, hypervisorPath, virtualMachineCode);
                }
                break;
            case VM_TIME:
                System.out.println("The Clouder Server operation request is VM_TIME");
                if (message.length > 4) {
                    int executionTime = message.getInteger(3);
                    String id = message.getString(4);
                    PersistentExecutionManager.extendsVMTime(id, executionTime);
                }
                break;
            default:
            //System.err.println(clouderClientOperationResult);
        }
    }

    /**
     * Method responsible for attending requests for operations over the
     * physical machine
     *
     * @param clouderServerRequestSplitted Server request
     * @param con Channel used to interact with UnaCloud server to recieve or
     * send additional data
     */
    private void PHYSICAL_MACHINE_OPERATION(UnaCloudMessage message) {
        String clouderClientOperationResult = "";
        // clouderServerRequestSplitted[1] = {PM_TURN_OFF, PM_RESTART, PM_LOGOUT, PM_MONITOR}
        int physicalOperationType = message.getInteger(1);
        switch (physicalOperationType) {
            case PM_TURN_OFF:
                clouderClientOperationResult = "PM_TURN_OFF";
                clouderClientOperationResult += MESSAGE_SEPARATOR_TOKEN + new OperatingSystem().turnOff();
                break;
            case PM_RESTART:
                clouderClientOperationResult = "PM_RESTART";
                clouderClientOperationResult += MESSAGE_SEPARATOR_TOKEN + new OperatingSystem().restart();
                break;
            case PM_LOGOUT:
                clouderClientOperationResult = "PM_LOGOUT";
                clouderClientOperationResult += MESSAGE_SEPARATOR_TOKEN + new OperatingSystem().logOut();
                break;
                //TODO do something
            /*case PM_WRITE_FILE:
                clouderClientOperationResult = "PM_WRITE_FILE";
                FileTrasferAttender.attendFileOperation(message, con);
                break;*/
            case PM_TURN_ON:
                String[] macs = message.getStrings(2, message.length);
                for (String mac : macs) {
                    try {
                        Runtime.getRuntime().exec("wol.exe " + mac.replace(":", ""));
                    } catch (IOException ex) {
                    }
                }
                break;
            case PM_MONITOR:
                if (message.length > 2) {
                    String op = message.getString(2);
                    if (op.equals("STOP")) {
                        PhysicalMachineMonitor.stop();
                    } else if (op.equals("START") && message.length > 4) {
                        int fm = message.getInteger(3);
                        int fr = message.getInteger(4);
                        PhysicalMachineMonitor.start(fm, fr);
                    }
                }
                break;
                //TODO do something
            /*case PM_RETRIEVE_FOLDER:
                clouderClientOperationResult = "MACHINE_RESTORE";
                if (message.length > 2) {
                    try {
                        new UnicastSender().attendFileRetrieveRequest(message, communication);
                    } catch (Exception e) {
                        clouderClientOperationResult += ERROR_MESSAGE + " " + e.getMessage();
                    }

                } else {
                    clouderClientOperationResult += ERROR_MESSAGE + "invalid number of parameters: " + message.length;
                }
                break;*/
            default:
                clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server physical machine operation request is invalid: " + physicalOperationType;
            //System.err.println(clouderClientOperationResult);
        }
    }
}
