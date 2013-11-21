/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import communication.messages.PhysicalMachineOperationMessage;
import communication.messages.VirtualMachineOperationMessage;

/**
 *
 * @author Clouder
 */
public abstract class UnaCloudAbstractMessage{
    //UnaCloud Server operation request constants
    public static final int VIRTUAL_MACHINE_OPERATION = 1;
    public static final int PHYSICAL_MACHINE_OPERATION = 2;
    public static final int AGENT_OPERATION = 3;
    
    //UnaCloud Client operation request constants
    public static final int DATABASE_OPERATION = 1;
    public static final int REGISTRATION_OPERATION = 2;
    
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
    public static UnaCloudAbstractMessage fromMessage(UnaCloudMessage message){
    	switch(message.getInteger(0)){
    		case PHYSICAL_MACHINE_OPERATION:
    			return PhysicalMachineOperationMessage.fromMessage(message);
    		case VIRTUAL_MACHINE_OPERATION:
    			return VirtualMachineOperationMessage.fromMessage(message);
    	}
    	return null;
    }
}
