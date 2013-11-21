package communication;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.losandes.utils.Log;
import com.losandes.utils.VariableManager;

import java.io.Closeable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Responsible for listening to the Clouder Server
 */
public class ClouderClientAttention implements Closeable{
	
	public static int POOL_THREAD_SIZE = 2;

	private ServerSocket serverSocket;
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
        	serverSocket = new ServerSocket(localPort);
            while (true) {
                try {
                	Socket sSocket = serverSocket.accept();
                    poolExe.execute(new ClouderServerAttentionThread(sSocket, this));
                } catch (IOException ex) {
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
        	serverSocket.close();
        } catch (Exception e) {
        }
    }
}