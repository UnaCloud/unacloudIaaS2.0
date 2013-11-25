package communication.messages.vmo;

import communication.messages.VirtualMachineOperationMessage;

public class VirtualMachineAddTimeMessage extends VirtualMachineOperationMessage{
	private static final long serialVersionUID = 7525891768407688888L;
	int executionTime;
    String id;
    public VirtualMachineAddTimeMessage() {
		super(VM_TIME);
	}
	public int getExecutionTime() {
		return executionTime;
	}
	public String getId() {
		return id;
	}
}