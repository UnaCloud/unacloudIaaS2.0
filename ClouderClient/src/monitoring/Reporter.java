package monitoring;

import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
import com.losandes.communication.security.SecureSocket;
import com.losandes.utils.VariableManager;
import static com.losandes.utils.Constants.*;

/**
 * Class responsible for report this physical machine status. Every 30 seconds this class sends a keep alive message to UnaCloud server.
 * @author Clouder
 */
public class Reporter extends Thread{

    /**
     * Period time for the reporing process
     */
    private final int sleepTime;

    /**
     * Id to be used to report this physical machine, It corresponds to the physical machine name
     */
    private final String id;

    /**
     * Constructs a physical machine reporter
     * @param id Id to be used to report this physical machine, It corresponds to the physical machine name
     * @param sleep How much should the reporter wait between reports
     */
    public Reporter(String id,int sleep){
        this.sleepTime = sleep;
        this.id=id;
    }

    /**
     * Method that is used to start the reporting thread
     */
    @Override
    public void run() {
       int fails = 0;SecureSocket conection;
       try{
           conection=new SecureSocket(VariableManager.getStringValue("CLOUDER_SERVER_IP"),VariableManager.getIntValue("CLOUDER_SERVER_PORT"));;
       }catch(ConnectionException e){
           return;
       }

       while(true){
           try{
               AbstractCommunicator ac = conection.connect();
               ac.writeUTF(""+DATABASE_OPERATION,""+REPORT_DB,id);
               ac.close();
               fails=0;
           }catch(ConnectionException sce){
               fails++;
           }
           try{
               sleep(sleepTime);
           }catch(Exception e){
           }
       }
    }


}

