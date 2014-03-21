package communication;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.losandes.utils.VariableManager;

import java.net.ServerSocket;
import java.net.Socket;

import tasks.ExecutorService;

/**
 * Responsible for listening to the Clouder Server
 */
public class ClouderClientAttention{
        
    private ServerSocket serverSocket;
    /**
     * Port to be used by the listening server socket
     */
    private static int localPort;

    private static ClouderClientAttention instance;
    public synchronized static ClouderClientAttention getInstance() {
            if(instance==null)instance=new ClouderClientAttention();
                return instance;
        }
    /**
     * Responsible for obtaining data connection and listening to Clouder Server
     */
    private ClouderClientAttention() {
        localPort = VariableManager.global.getIntValue("CLOUDER_CLIENT_PORT");
    }

    /**
     * Responsible for connecting with Clouder Server and start a communication thread
     */
    public final void connect() {
        try {
			serverSocket = new ServerSocket(localPort);
			System.out.println("Escuchando en "+localPort);
	        while (true) {
	        	Socket s;
	            try {
	            	s=serverSocket.accept();
	            	System.out.println("Atendiendo");
	            } catch (IOException ex) {
	            	ex.printStackTrace();
	            	break;
	            }
	            try {
	            	ExecutorService.execute(new ClouderServerAttentionThread(s));
	            } catch (Exception ex) {
	            	ex.printStackTrace();
	            	break;
	            }
                
	        }
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    }

    /**
     * Stops the request listening process
     */
    public static void close() {
        try {
            instance.serverSocket.close();
        } catch (Exception e) {
        }
    }
}