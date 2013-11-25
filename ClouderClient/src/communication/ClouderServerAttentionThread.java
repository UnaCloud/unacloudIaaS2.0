package communication;

import static com.losandes.utils.Constants.ERROR_MESSAGE;
import static com.losandes.utils.Constants.MESSAGE_SEPARATOR_TOKEN;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import monitoring.PhysicalMachineMonitor;
import physicalmachine.OperatingSystem;
import virtualMachineConfiguration.AbstractVirtualMachineConfigurator;
import virtualMachineExecution.PersistentExecutionManager;
import communication.messages.AgentMessage;
import communication.messages.InvalidOperationResponse;
import communication.messages.PhysicalMachineOperationMessage;
import communication.messages.VirtualMachineOperationMessage;
import communication.messages.pmo.PhysicalMachineMonitorMessage;
import communication.messages.pmo.PhysicalMachineTurnOnMessage;
import communication.messages.vmo.VirtualMachineAddTimeMessage;
import communication.messages.vmo.VirtualMachineRestartMessage;
import communication.messages.vmo.VirtualMachineStopMessage;
import communication.messages.vmo.VirtualMachineStartMessage;

/**
 * Responsible for attending or discarding a Clouder
 * Server operation request in a thread
 */
public class ClouderServerAttentionThread extends Thread {

    /**
     * Abstract comunnicator used to recieve the request and send a response
     */
    private Socket communication;
    
    /**
     * Constructs an attention thread for a given communicator channel. Recieves
     * an owner object for stop requests
     *
     * @param clientSocket
     * @param cca The owner of this object
     */
    public ClouderServerAttentionThread(Socket clientSocket) {
        communication = clientSocket;
    }

    /**
     * Responsible for attending or discarding the Clouder Server operation
     * request
     */
    public void run() {
        try(ObjectInputStream ois=new ObjectInputStream(communication.getInputStream());ObjectOutputStream oos=new ObjectOutputStream(communication.getOutputStream())){
        	for(UnaCloudAbstractMessage clouderServerRequest;(clouderServerRequest = (UnaCloudAbstractMessage)ois.readObject())!=null;){
        		switch (clouderServerRequest.getMainOp()) {
	                case UnaCloudAbstractMessage.VIRTUAL_MACHINE_OPERATION:
	                    oos.writeObject(attendVirtualMachineOperation(clouderServerRequest,ois,oos));
	                    break;
	                case UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION:
	                    attendPhysicalMachineOperation(clouderServerRequest);
	                    break;
	                case UnaCloudAbstractMessage.AGENT_OPERATION:
	                    attendAgentOperation(clouderServerRequest);
	                    break;
	                default:
	                	//TODO do something
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
     * Method responsible for attending requests for operations over virtual machines
     * @param clouderServerRequestSplitted Server request
     */
    private UnaCloudAbstractResponse attendVirtualMachineOperation(UnaCloudAbstractMessage message,ObjectInputStream ois,ObjectOutputStream pw) {
        switch (message.getSubOp()) {
            case VirtualMachineOperationMessage.VM_START:
            	return AbstractVirtualMachineConfigurator.startVirtualMachine((VirtualMachineStartMessage)message);
            case VirtualMachineOperationMessage.VM_STOP:
            	return PersistentExecutionManager.removeExecution(((VirtualMachineStopMessage)message).getVirtualMachineExecutionId());
            case VirtualMachineOperationMessage.VM_RESTART:
            	return PersistentExecutionManager.restartMachine((VirtualMachineRestartMessage)message);
            case VirtualMachineOperationMessage.VM_TIME:
            	return PersistentExecutionManager.extendsVMTime((VirtualMachineAddTimeMessage)message);
            default:
            	return new InvalidOperationResponse("Invalid virtual machine operation: "+message.getSubOp());
        }
    }
    private String attendAgentOperation(UnaCloudAbstractMessage message) {
    	switch (message.getSubOp()) {
    		case AgentMessage.UPDATE_OPERATION:
    			ClouderClientAttention.close();
    			try {
                    Runtime.getRuntime().exec("javaw -jar ClientUpdater.jar 6");
                } catch (Exception e) {
                }
                System.exit(6);
    			break;
    		case AgentMessage.STOP_CLIENT:
    			ClouderClientAttention.close();
    			System.exit(0);
    			break;
    		case AgentMessage.GET_VERSION:
    			//TODO imprimir version
                //pw.println("1.30");
    	}
    	return null;
    }
    /**
     * Method responsible for attending requests for operations over the
     * physical machine
     *
     * @param clouderServerRequestSplitted Server request
     * @param con Channel used to interact with UnaCloud server to recieve or
     * send additional data
     */
    private String attendPhysicalMachineOperation(UnaCloudAbstractMessage message) {
        switch (message.getSubOp()) {
            case PhysicalMachineOperationMessage.PM_TURN_OFF:
                return "PM_TURN_OFF" + MESSAGE_SEPARATOR_TOKEN + new OperatingSystem().turnOff();
            case PhysicalMachineOperationMessage.PM_RESTART:
                return "PM_RESTART" + MESSAGE_SEPARATOR_TOKEN + new OperatingSystem().restart();
            case PhysicalMachineOperationMessage.PM_LOGOUT:
                return "PM_LOGOUT" + MESSAGE_SEPARATOR_TOKEN + new OperatingSystem().logOut();
                //TODO do something
            /*case PM_WRITE_FILE:
                clouderClientOperationResult = "PM_WRITE_FILE";
                FileTrasferAttender.attendFileOperation(message, con);
                break;*/
            case PhysicalMachineOperationMessage.PM_TURN_ON:
            	PhysicalMachineTurnOnMessage turnOn=(PhysicalMachineTurnOnMessage)message;
                for (String mac : turnOn.getMacs()) {
                    try {
                        Runtime.getRuntime().exec("wol.exe " + mac.replace(":", ""));
                    } catch (IOException ex) {
                    }
                }
                return "Successful operation";
            case PhysicalMachineOperationMessage.PM_MONITOR:
            	PhysicalMachineMonitorMessage monitor=(PhysicalMachineMonitorMessage)message;
            	switch (monitor.getOperation()) {
					case "STOP":
						PhysicalMachineMonitor.stop();
						break;
					case "START":
						PhysicalMachineMonitor.start(monitor.getMonitorFrequency(),monitor.getRegisterFrequency());
						break;
				}
            	return "Successful operation";
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
                return ERROR_MESSAGE + "The Clouder Server physical machine operation request is invalid: " + message.getSubOp();
        }
    }
}
