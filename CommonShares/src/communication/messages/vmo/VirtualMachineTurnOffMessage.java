package communication.messages.vmo;

import communication.UnaCloudMessage;
import communication.messages.VirtualMachineOperationMessage;

public class VirtualMachineTurnOffMessage extends VirtualMachineOperationMessage{
	int hypervisorName;
    String vmPath;
    String hypervisorPath;
    public VirtualMachineTurnOffMessage() {
		super(VM_TURN_OFF);
	}
    @Override
    protected void processMessage(UnaCloudMessage message) {
    	virtualMachineExecutionId = message.getString(2);
    	hypervisorName = message.getInteger(3);
        vmPath = message.getString(4);
        hypervisorPath = message.getString(5);
    }
	public int getHypervisorName() {
		return hypervisorName;
	}
	public String getVmPath() {
		return vmPath;
	}
	public String getHypervisorPath() {
		return hypervisorPath;
	}
    
}
