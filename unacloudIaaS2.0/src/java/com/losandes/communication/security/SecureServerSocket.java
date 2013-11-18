/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.communication.security;

import com.losandes.communication.security.utils.*;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Secure server socket used to listen requests from unacloud clients
 * @author Clouder
 */
public class SecureServerSocket {

    /**
     * Server socket used to listen on a TCP port
     */
    private ServerSocket ss;

    /**
     * Creates a new secure server socket listening in the given port
     * @param port
     * @throws IOException
     */
    public SecureServerSocket(int port) throws IOException{
        ss = new ServerSocket(port);
    }

    /**
     * Accepts a new request for this server socket
     * @return
     * @throws ConnectionException
     * @throws IOException
     */
    public AbstractCommunicator accept() throws ConnectionException, IOException{
        SecureServerStream ssss = new SecureServerStream();
        ssss.connect(ss.accept());
        return ssss;
    }

    /**
     * Closes this secure server socket
     */
    public void close(){
        try {
            ss.close();
        } catch (IOException ex) {
        }
    }
}
