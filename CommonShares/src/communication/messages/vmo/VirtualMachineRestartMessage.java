package communication.messages.vmo;

import communication.messages.VirtualMachineOperationMessage;

public class VirtualMachineRestartMessage extends VirtualMachineOperationMessage{
	private static final long serialVersionUID = 619421995819548819L;
	int hypervisorName;
    String vmPath;
    String hypervisorPath;
    public VirtualMachineRestartMessage() {
		super(VM_RESTART);
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