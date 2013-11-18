/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.multicast;

/**
 *
 * @author Clouder
 */
@Deprecated
public class MulticastChannel {

    public final String ip;
    public final int puerto;

    public MulticastChannel(String ip, int puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }

    
}
