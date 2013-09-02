package com.losandes.communication.security;

import com.losandes.communication.security.utils.SecureClientStream;
import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server socket used to establish secure connections at the unacloud client side
 * @author Clouder
 */
public class SecureServerSocket {

    /**
     * Java server sockect used to accept TCP connections
     */
    private ServerSocket ss;

    /**
     * Initializes a secure server socket listening on the given port
     * @param port A TCP port to listen for server connections
     * @throws IOException Throws an exception if there is an error opening the socket on the given port
     */
    public SecureServerSocket(int port) throws IOException{
        ss = new ServerSocket(port);
    }

    /**
     * Accepts a new TCP encrypted connection using this class socket
     * @return An abstract class representing the new connection
     * @throws ConnectionException if there is an error establishing a secure connection
     * @throws IOException if there is an error listening on the socket port
     */
    public AbstractCommunicator accept() throws ConnectionException, IOException{
        SecureClientStream ssss = new SecureClientStream();
        ssss.connect(ss.accept());
        return ssss;
    }

    /**
     * Closes this server socket
     */
    public void close(){
        try {
            ss.close();
        } catch (IOException ex) {
        }
    }
}
