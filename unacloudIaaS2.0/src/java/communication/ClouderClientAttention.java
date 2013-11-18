package communication;


import static com.losandes.utils.Constants.ERROR_MESSAGE;
import static com.losandes.utils.Constants.LOGIN_DB;
import static com.losandes.utils.Constants.LOGOUT_DB;
import static com.losandes.utils.Constants.MESSAGE_SEPARATOR_TOKEN;
import static com.losandes.utils.Constants.NOTHING_AVAILABLE;
import static com.losandes.utils.Constants.OFF_STATE;
import static com.losandes.utils.Constants.ON_STATE;
import static com.losandes.utils.Constants.PM_LOGOUT;
import static com.losandes.utils.Constants.PM_MONITOR;
import static com.losandes.utils.Constants.PM_RESTART;
import static com.losandes.utils.Constants.PM_TURN_OFF;
import static com.losandes.utils.Constants.TURN_OFF_DB;
import static com.losandes.utils.Constants.TURN_ON_DB;
import static com.losandes.utils.Constants.VIRTUAL_MACHINE_CPU_STATE;
import static com.losandes.utils.Constants.VIRTUAL_MACHINE_STATE_DB;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.messages.UnaCloudMessage;
import com.losandes.persistence.PersistenceServices;
import com.losandes.utils.VirtualMachineCPUStates;

/**
 * @author German Sotelo
 * Responsible for attending or discarding a Clouder Server operation request in a thread
 */
public class ClouderClientAttention{

    private static MachineStateManager machineManager = new MachineStateManager();;

    public static String attendRequest(String clouderServerRequestString){
    	return attendRequest(new UnaCloudMessage(clouderServerRequestString));
    }
    /**
     * Responsible for sorting or discarding the Clouder Server operation request
     */
    public static String attendRequest(UnaCloudMessage clouderServerRequest){
    	if(clouderServerRequest==null)return null;
    	//clouderClientOperationResult is the result variable for responding to Clouder Server
    	String clouderClientOperationResult = "";
    	
        try {
            // operationDomain = {VIRTUAL_MACHINE_OPERATION, PHYSICAL_MACHINE_OPERATION}
            int operationDomain = clouderServerRequest.getInteger(0);

            if (operationDomain != 0 && operationDomain < 3) {
                if (operationDomain == UnaCloudAbstractMessage.DATABASE_OPERATION) {
                    int OperationType = clouderServerRequest.getInteger(1);
                    String physicalMachineName = clouderServerRequest.getString(2);
                    PersistenceServices persistence = new PersistenceServices();
                    switch (OperationType) {
                        case TURN_ON_DB:
                            persistence.updatePhysicalMachineState(ON_STATE,physicalMachineName);
                            //REPORT_DELAY,REPORT_FAIL_LIMIT
                            return (MachineStateManager.period+MESSAGE_SEPARATOR_TOKEN+MachineStateManager.limitFail);
                        case TURN_OFF_DB:
                            persistence.updatePhysicalMachineState(OFF_STATE,physicalMachineName);
                            break;
                        case LOGIN_DB:
                            String physicalMachineUser=clouderServerRequest.getString(3);
                            persistence.logginPhysicalMachineUser(physicalMachineName, physicalMachineUser);
                            machineManager.reportMachine(physicalMachineName);
                            break;
                        case LOGOUT_DB:
                            persistence.logginPhysicalMachineUser(physicalMachineName, NOTHING_AVAILABLE);
                            break;
                        case VIRTUAL_MACHINE_STATE_DB:
                            persistence.updateVirtualMachineState(clouderServerRequest.getString(3),clouderServerRequest.getInteger(4),clouderServerRequest.getString(5));
                            machineManager.reportMachine(physicalMachineName);
                            break;
                        case VIRTUAL_MACHINE_CPU_STATE:
                            persistence.updateVirtualMachineCPUState(clouderServerRequest.getString(3),VirtualMachineCPUStates.valueOf(clouderServerRequest.getString(4)));
                            break;
                        default:
                            clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Client operation request is invalid: " + OperationType;
                            System.err.println(clouderClientOperationResult);
                            System.out.println(clouderServerRequest);
                    }
                } else if (operationDomain == UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION) {
                    System.out.println("The Clouder Server operation request is physical machine type");
                    // TODO por definirse
                    int physicalOperationType = clouderServerRequest.getInteger(1);
                    switch (physicalOperationType) {
                        case PM_TURN_OFF:
                            break;
                        case PM_RESTART:
                            break;
                        case PM_LOGOUT:
                            break;
                        case PM_MONITOR:
                            break;
                        default:
                            clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server physical machine operation request is invalid: " + physicalOperationType;
                            System.err.println(clouderClientOperationResult);
                            System.out.println(clouderServerRequest);
                    }
                } else {
                    clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server request type is invalid: " + operationDomain;
                    System.err.println(clouderClientOperationResult);
                    System.out.println(clouderServerRequest);
                }
            } else {
                clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server request is null or an invalid number: " + operationDomain;
                System.err.println(clouderClientOperationResult);
            }
        } catch (Exception ex) {
            System.err.println("The communication process with Clouder Server failed in ClouderServerAttentionThread: " + ex.getMessage());
        }
        return clouderClientOperationResult; 
    }
}

