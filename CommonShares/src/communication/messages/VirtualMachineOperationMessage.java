package communication.messages;

import communication.UnaCloudAbstractMessage;
import communication.UnaCloudMessage;

public abstract class VirtualMachineOperationMessage extends UnaCloudAbstractMessage{
	private static final long serialVersionUID = -719111911251582119L;
	public static final int VM_TURN_ON = 1;
    public static final int VM_TURN_OFF = 2;
    public static final int VM_RESTART = 3;
    public static final int VM_STATE = 4;
    public static final int VM_TIME = 5;
    public static final int VIRTUAL_MACHINE_CONFIGURATION=6;
    protected String virtualMachineExecutionId; 
	public VirtualMachineOperationMessage(int subOperation){
		super(VIRTUAL_MACHINE_OPERATION,subOperation);
	}
	public VirtualMachineOperationMessage(int subOperation,UnaCloudMessage message){
		super(VIRTUAL_MACHINE_OPERATION,subOperation,message);
	}
	public static UnaCloudAbstractMessage fromMessage(UnaCloudMessage message){
		return null;
	}
	public String getVirtualMachineExecutionId() {
		return virtualMachineExecutionId;
	}
}
