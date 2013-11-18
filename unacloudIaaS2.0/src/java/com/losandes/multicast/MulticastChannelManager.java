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
public class MulticastChannelManager{

    private static MulticastChannel[] canales;
    private static boolean[] states;

    public MulticastChannelManager() {
        if(canales==null){
            canales=new MulticastChannel[10];
            states=new boolean[canales.length];
            for(int e=0;e<canales.length;e++){
                canales[e]=new MulticastChannel("225.0.0."+(1+e),1000);
            }
        }
    }

    public synchronized MulticastChannel getAviableMulticastChannel(){
        for(int e=0;e<canales.length;e++)if(!states[e]){
            states[e]=true;
            return canales[e];
        }
        return null;
    }

    public synchronized void freeMulticastChannel(MulticastChannel channel){
        for(int e=0;e<canales.length;e++)if(canales[e].equals(channel)){
            states[e]=false;
            return;
        }
    }
}
