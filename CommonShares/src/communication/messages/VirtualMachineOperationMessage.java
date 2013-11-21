package communication.messages;

import communication.UnaCloudAbstractMessage;
import communication.UnaCloudMessage;

public abstract class VirtualMachineOperationMessage extends UnaCloudAbstractMessage{
	public static final int VM_TURN_ON = 1;
    public static final int VM_TURN_OFF = 2;
    public static final int VM_RESTART = 3;
    public static final int VM_STATE = 4;
    public static final int VM_TIME = 5;
    protected String virtualMachineExecutionId; 
	public VirtualMachineOperationMessage(int subOperation){
		super(VIRTUAL_MACHINE_OPERATION,subOperation);
	}
	public static UnaCloudAbstractMessage fromMessage(UnaCloudMessage message){
		return null;
	}
	public String getVirtualMachineExecutionId() {
		return virtualMachineExecutionId;
	}
}
