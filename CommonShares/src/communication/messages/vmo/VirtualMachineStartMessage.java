package communication.messages.vmo;

import communication.UnaCloudMessage;
import communication.messages.VirtualMachineOperationMessage;

public class VirtualMachineStartMessage extends VirtualMachineOperationMessage implements Comparable<VirtualMachineStartMessage>{
	private static final long serialVersionUID = -5116988985857543662L;
	int hypervisorName,vmCores,vmMemory;
    String vmPath,hypervisorPath;
    int executionTime;
    String virtualMachineIP,virtualMachineNetMask,checkPoint,snapshotRoute;
    boolean persistent;
    long shutdownTime;
    String username,password;
    public VirtualMachineStartMessage() {
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
        virtualMachineIP = message.getString(9);
        persistent = Boolean.parseBoolean(message.getString(10));
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
		return virtualMachineIP;
	}
	public boolean isPersistent() {
		return persistent;
	}
	public String getCheckPoint() {
		return checkPoint;
	}
	public String getSnapshotRoute() {
		return snapshotRoute;
	}
	public long getShutdownTime() {
		return shutdownTime;
	}
	public void setShutdownTime(long shutdownTime) {
		this.shutdownTime = shutdownTime;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getVirtualMachineNetMask() {
		return virtualMachineNetMask;
	}
	@Override
	public int compareTo(VirtualMachineStartMessage o) {
		return getVirtualMachineExecutionId().compareTo(o.getVirtualMachineExecutionId());
	}
	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}
	
}
