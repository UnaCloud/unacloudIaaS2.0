package communication.messages.vmo;

import communication.UnaCloudMessage;
import communication.messages.VirtualMachineOperationMessage;

public class VirtualMachineTurnOnMessage extends VirtualMachineOperationMessage{
	int hypervisorName,vmCores,vmMemory;
    String vmPath,hypervisorPath;
    int executionTime;
    String vmIP,persistent,checkPoint,snapshotRoute;
    public VirtualMachineTurnOnMessage() {
		super(VM_TURN_ON);
	}
    @Override
    protected void processMessage(UnaCloudMessage message) {
    	virtualMachineExecutionId = message.getString(2);
    	hypervisorName = message.getInteger(3);
        vmCores = message.getInteger(4);
        vmMemory = message.getInteger(5);
        vmPath = message.getString(6);
        hypervisorPath = message.getString(7);
        executionTime = message.getInteger(8);
        vmIP = message.getString(9);
        persistent = message.getString(10);
        checkPoint = message.getString(11);
        snapshotRoute = message.getString(12);
    }
	public int getHypervisorName() {
		return hypervisorName;
	}
	public int getVmCores() {
		return vmCores;
	}
	public int getVmMemory() {
		return vmMemory;
	}
	public String getVmPath() {
		return vmPath;
	}
	public String getHypervisorPath() {
		return hypervisorPath;
	}
	public int getExecutionTime() {
		return executionTime;
	}
	public String getVmIP() {
		return vmIP;
	}
	public String getPersistent() {
		return persistent;
	}
	public String getCheckPoint() {
		return checkPoint;
	}
	public String getSnapshotRoute() {
		return snapshotRoute;
	}
}
