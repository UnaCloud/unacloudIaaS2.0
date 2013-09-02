package communication;

import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
import com.losandes.communication.security.SecureServerSocket;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.losandes.utils.Log;
import com.losandes.utils.VariableManager;
import java.io.Closeable;
import static com.losandes.utils.Constants.*;

/**
 * @author Eduardo Rosales
 * Responsible for listening to the Clouder Server
 */
public class ClouderClientAttention implements Closeable{

    /**
     * Secure server socket used to listen to UnaCloud server requests
     */
    private SecureServerSocket clouderClientServerSocket;

    /**
     * Port to be used by the listening server socket
     */
    private static int localPort;

    /**
     * A pool of threads used to attend UnaCloud server requests
     */
    private Executor poolExe;

    /**
     * Responsible for obtaining data connection and listening to Clouder Server
     */
    public ClouderClientAttention() {
        poolExe = Executors.newFixedThreadPool(POOL_THREAD_SIZE);
        localPort = VariableManager.getIntValue("CLOUDER_CLIENT_PORT");
        connect();
    }

    /**
     * Responsible for connecting with Clouder Server and start a communication thread
     */
    public final void connect() {
        try {
            clouderClientServerSocket = new SecureServerSocket(localPort);
            while (true) {
                AbstractCommunicator sSocket;
                try {
                    sSocket = clouderClientServerSocket.accept();
                    poolExe.execute(new ClouderServerAttentionThread(sSocket, this));
                } catch (ConnectionException ex) {
                    Log.print("Can't process message " + localPort + " . " + ex.getLocalizedMessage());
                }
            }
        } catch (IOException ex) {
            Log.print("Can't open socket on the port " + localPort + ". " + ex.getLocalizedMessage());
        }
    }

    /**
     * Stops the request listening process
     */
    public void close() {
        try {
            clouderClientServerSocket.close();
        } catch (Exception e) {
        }
    }
}//end of ClouderClientAttention

