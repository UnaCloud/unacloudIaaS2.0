package communication;

import static com.losandes.utils.Constants.VIRTUAL_MACHINE_CPU_STATE;
import static com.losandes.utils.Constants.VIRTUAL_MACHINE_STATE_DB;
import physicalmachine.Network;

import com.losandes.utils.VirtualMachineCPUStates;
import communication.messages.UnaCloudAbstractMessage;

/**
 * Class used to send quickly messages to UnaCloud server
 * @author Clouder
 */
public class ServerMessageSender {

    /**
     * This is a static class
     */
    private ServerMessageSender() {
    }

    /**
     * Sends an information message to UnaCloud server and does'n wait for response
     * @param message The message to be send
     * @return if the message could be sent or not
     */
    public static boolean sendMessage(Object ... message){
    	return AbstractGrailsCommunicator.doRequest(message)!=null;
    }

    /**
     * Sends to UnaCloud server a message reporting the state of a virtual machine
     * @param virtualMachineCode The string that contains the id of the virtual machine to be reported
     * @param state The state of the reported virtual machine
     * @param message The state message of the reported virtual machine
     * @return If the message could be sent or not
     */
    public static boolean reportVirtualMachineState(String virtualMachineCode,int state,String message){
        return sendMessage(UnaCloudAbstractMessage.DATABASE_OPERATION,VIRTUAL_MACHINE_STATE_DB,Network.getHostname(),virtualMachineCode,state,message);
    }

    /**
     * Sends a message reporting the CPU state of a virtual machine to UnaCloud server
     * @param virtualMachineCode The id of the virtual machine to be reported
     * @param cpustate The state of the CPU of the reported virtual machine
     * @return If the message could be sent or not
     */
    public static boolean reportVirtualMachineCPUState(String virtualMachineCode,VirtualMachineCPUStates cpustate){
        return sendMessage(UnaCloudAbstractMessage.DATABASE_OPERATION,VIRTUAL_MACHINE_CPU_STATE,Network.getHostname(),virtualMachineCode,cpustate);
    }

}
