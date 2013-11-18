package domain;

import com.losandes.dataChannel.DataServerSocket;
import communication.MachineStateManager;

/**
 * @author Eduardo Rosales
 * Responsible for starting the Clouder State Server
 *
 */
public class Main {

    public static void main(String[] args) {
    	//TODO mirar quien ejecuta esto.
    	//TODO tambien averiguar que hace el log listener
        DataServerSocket.init();
    }
}//end of Main

