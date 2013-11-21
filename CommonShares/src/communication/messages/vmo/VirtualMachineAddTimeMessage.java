package communication.messages.vmo;

import communication.UnaCloudMessage;
import communication.messages.VirtualMachineOperationMessage;

public class VirtualMachineAddTimeMessage extends VirtualMachineOperationMessage{
	int executionTime;
    String id;
    public VirtualMachineAddTimeMessage() {
		super(VM_TIME);
	}
    @Override
    protected void processMessage(UnaCloudMessage message) {
    	virtualMachineExecutionId = message.getString(2);
    	executionTime = message.getInteger(3);
        id = message.getString(4);
    }
	public int getExecutionTime() {
		return executionTime;
	}
	public String getId() {
		return id;
	}
}