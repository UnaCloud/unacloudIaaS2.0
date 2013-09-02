package com.losandes.communication.security;

import com.losandes.communication.security.utils.SecureClientStream;
import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
import java.net.Socket;

/**
 * Socket used to establish secure connections at the unacloud client side to the unacloud server
 * @author Clouder
 */
public class SecureSocket {

    /**
     * The host that represents UnaCloud Server
     */
    private String host;
    /**
     * The port used to connect to UnaCloud Server
     */
    private int port;
    /**
     * A secure stream used to encrypt adn decript the data transmitted through this socket
     */
    private SecureClientStream scss;

    /**
     * Constructs a secure socket connection using the given host and listen port
     * @param host The host to connect to
     * @param port The port in which the host is listening
     * @throws ConnectionException if there is an error securing the connection
     */
    public SecureSocket(String host, int port) throws ConnectionException {
        this.host = host;
        this.port = port;
        scss=new SecureClientStream();
    }

    /**
     * Connects this socket to the remote host endpoint and return a secure communicator to secure transmitted data
     * @return A secure abstract communicator that will be used to transmit secure data over this socket
     * @throws ConnectionException if there is an error securing the channel or connecting to the host
     */
    public AbstractCommunicator connect() throws ConnectionException{
        Socket s;
        try {
            s = new Socket(host, port);
        } catch (Exception ex) {
            throw new ConnectionException("Can't connect to "+host);
        }
        scss.connect(s);
        return scss;
    }



}
