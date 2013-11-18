/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.communication.messages;

/**
 *
 * @author Clouder
 */
public abstract class UnaCloudAbstractMessage<T>{
    //UnaCloud Server operation request constants
    public static final int VIRTUAL_MACHINE_OPERATION = 1;
    public static final int PHYSICAL_MACHINE_OPERATION = 2;
    
    //UnaCloud Client operation request constants
    public static final int DATABASE_OPERATION = 1;
    public static final int REGISTRATION_OPERATION = 2;
    public static final int ARTHUR_OPERATION = 3;
    public static final int UPDATE_OPERATION = 4;
    public static final int VIRTUAL_MACHINE_CONFIGURATION=5;
    
    private int mainOp;
    private int subOp;

    public UnaCloudAbstractMessage(int mainOp, int subOp){
        this.mainOp = mainOp;
        this.subOp = subOp;
    }

    public UnaCloudAbstractMessage(int mainOp, int subOp,UnaCloudMessage message){
        this(mainOp,subOp);
        processMessage(message);
    }
    public int getMainOp() {
        return mainOp;
    }

    public void setMainOp(int mainOp) {
        this.mainOp = mainOp;
    }

    public int getSubOp() {
        return subOp;
    }

    public void setSubOp(int subOp) {
        this.subOp = subOp;
    }
    protected abstract void processMessage(UnaCloudMessage message);
}
