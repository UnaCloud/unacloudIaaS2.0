package monitoring;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
import com.losandes.utils.VariableManager;
import communication.AbstractGrailsCommunicator;

import static com.losandes.utils.Constants.*;

/**
 * Class responsible for report this physical machine status. Every 30 seconds this class sends a keep alive message to UnaCloud server.
 * @author Clouder
 */
public class PhysicalMachineStateReporter extends Thread{

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
    public PhysicalMachineStateReporter(String id,int sleep){
        this.sleepTime = sleep;
        this.id=id;
    }

    /**
     * Method that is used to start the reporting thread
     */
    @Override
    public void run() {
       int fails = 0;
       if(!AbstractGrailsCommunicator.checkServerStatus()){
    	   return ;
       }
       while(true){
           try{
               String username=MonitorReportGenerator.getUserName();
               AbstractGrailsCommunicator.doRequest(UnaCloudAbstractMessage.DATABASE_OPERATION,LOGIN_DB,id,username);
               fails=0;
           }catch(Exception sce){
               fails++;
           }
           try{
               sleep(sleepTime);
           }catch(Exception e){
        	   break;
           }
       }
    }


}

