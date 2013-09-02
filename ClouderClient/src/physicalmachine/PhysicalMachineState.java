package physicalmachine;

import com.losandes.communication.messages.UnaCloudMessage;
import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
import com.losandes.communication.security.SecureSocket;
import com.losandes.utils.VariableManager;

import java.io.PrintWriter;

import static com.losandes.utils.Constants.*;

/**
 * @author Eduardo Rosales
 * Responsible for reporting the physical machine state to Clouder Server
 */
public class PhysicalMachineState {

    private SecureSocket socket;

    private int REPORT_DELAY=30000;

    private int REPORT_FAIL_LIMIT=5;


    public PhysicalMachineState() throws ConnectionException {
        socket = new SecureSocket(VariableManager.getStringValue("CLOUDER_SERVER_IP"),VariableManager.getIntValue("CLOUDER_SERVER_PORT"));
    }

    public String reportPhysicalMachine(String clientMessage,boolean readReportParameters) {
        //System.err.println(clientMessage);
        String ClouderClientResponse = null;
        try {
            AbstractCommunicator ac = socket.connect();
            if (clientMessage != null && !clientMessage.equals("")) {
                ac.writeUTF(clientMessage);
                if(readReportParameters){
                    System.out.println("ac respuesta");
                    UnaCloudMessage message=ac.readUTFList();
                    System.out.println("respuesta recibida");
                    REPORT_DELAY=message.getInteger(0);
                    REPORT_FAIL_LIMIT=message.getInteger(1);
                }
                ac.close();
            } else {
                ClouderClientResponse = ERROR_MESSAGE + "the message was null";
            }
        } catch (ConnectionException sce){
            //TODO cargar datos archivo
            sce.printStackTrace();
            System.out.println("Error al intentar conectarse al servidor. "+sce.getMessage());
        }
        return ClouderClientResponse;
    }

    public int getREPORT_DELAY() {
        return REPORT_DELAY;
    }

    public int getREPORT_FAIL_LIMIT() {
        return REPORT_FAIL_LIMIT;
    }

    
}//end of PhysicalMachineState

