package communication.messages.vmo;

import communication.messages.VirtualMachineOperationMessage;

public class VirtualMachineStopMessage extends VirtualMachineOperationMessage{
	private static final long serialVersionUID = -8728929759121643688L;
	int hypervisorName;
    String vmPath;
    String hypervisorPath;
    public VirtualMachineStopMessage() {
		super(VM_STOP);
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
	public void setHypervisorName(int hypervisorName) {
		this.hypervisorName = hypervisorName;
	}
	public void setVmPath(String vmPath) {
		this.vmPath = vmPath;
	}
	public void setHypervisorPath(String hypervisorPath) {
		this.hypervisorPath = hypervisorPath;
	}
}
