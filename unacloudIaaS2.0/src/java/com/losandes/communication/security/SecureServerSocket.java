/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.communication.security;

import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Clouder
 */
public class SecureServerSocket {

    private ServerSocket ss;

    public SecureServerSocket(int port) throws IOException{
        ss = new ServerSocket(port);
    }

    public AbstractCommunicator accept() throws ConnectionException, IOException{
        SecureServerStream ssss = new SecureServerStream();
        ssss.connect(ss.accept());
        return ssss;
    }

    public void close(){
        try {
            ss.close();
        } catch (IOException ex) {
        }
    }
}
