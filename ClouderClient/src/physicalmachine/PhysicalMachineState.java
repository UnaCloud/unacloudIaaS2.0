package physicalmachine;

import static com.losandes.utils.Constants.ERROR_MESSAGE;

import communication.AbstractGrailsCommunicator;

/**
 * Responsible for reporting the physical machine state to Clouder Server
 */
public class PhysicalMachineState {

    private int REPORT_DELAY=30000;

    private int REPORT_FAIL_LIMIT=5;


    public PhysicalMachineState(){
    }

    public String reportPhysicalMachine(String clientMessage,boolean readReportParameters) {
        //System.err.println(clientMessage);
        String ClouderClientResponse = null;
        if (clientMessage != null && !clientMessage.equals("")) {
            String ret=AbstractGrailsCommunicator.doRequest(clientMessage);
            if(readReportParameters){
                System.out.println("ac respuesta");
                System.out.println("respuesta recibida");
                //TODO, extract report delay and fail limit
                //REPORT_DELAY=message.getInteger(0);
                //REPORT_FAIL_LIMIT=message.getInteger(1);
            }
        } else {
            ClouderClientResponse = ERROR_MESSAGE + "the message was null";
        }
        return ClouderClientResponse;
    }

    public int getREPORT_DELAY() {
        return REPORT_DELAY;
    }

    public int getREPORT_FAIL_LIMIT() {
        return REPORT_FAIL_LIMIT;
    }

}
