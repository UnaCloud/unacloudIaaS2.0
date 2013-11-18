/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.communication.security;
import com.losandes.communication.security.utils.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Secure socket used to communicate with secure server sockets
 * @author Clouder
 */
public class SecureSocket {

    /**
     * The host address to wich this socket is connected
     */
    private String host;

    /**
     * Port used by this socket to connect to the server
     */
    private int port;

    /**
     * Secure stream used to send data over this socket
     */
    private SecureServerStream scss;

    /**
     * Constructs a secure socket communication with the given host using the given port
     * @param host
     * @param port
     * @throws ConnectionException
     */
    public SecureSocket(String host,int port) throws ConnectionException {
        this.host = host;
        this.port=port;
        scss=new SecureServerStream();
    }

    /**
     * Makes up the connection represented by this secure socket
     * @return
     * @throws ConnectionException
     */
    public AbstractCommunicator connect() throws ConnectionException{
        Socket s=new Socket();
        try {
            s.connect(new InetSocketAddress(host, port),10000);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ConnectionException("Can't connect to "+host+" in port "+port);
        }
        scss.connect(s);
        return scss;
    }



}
