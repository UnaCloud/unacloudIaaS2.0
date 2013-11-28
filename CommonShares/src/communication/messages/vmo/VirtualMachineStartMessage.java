package communication.messages.vmo;

import communication.messages.VirtualMachineOperationMessage;

public class VirtualMachineStartMessage extends VirtualMachineOperationMessage implements Comparable<VirtualMachineStartMessage>{
	private static final long serialVersionUID = -5116988985857543662L;
	
	int vmCores,vmMemory;
    int executionTime;
    String virtualMachineIP,virtualMachineNetMask,snapshotRoute;
    boolean persistent;
    long shutdownTime;
    String hostname;
    
    public VirtualMachineStartMessage() {
		super(VM_START);
	}
	public int getVmCores() {
		return vmCores;
	}
	public int getVmMemory() {
		return vmMemory;
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
	public String getSnapshotRoute() {
		return snapshotRoute;
	}
	public long getShutdownTime() {
		return shutdownTime;
	}
	public void setShutdownTime(long shutdownTime) {
		this.shutdownTime = shutdownTime;
	}
	public String getVirtualMachineNetMask() {
		return virtualMachineNetMask;
	}
	@Override
	public int compareTo(VirtualMachineStartMessage o) {
		return Long.compare(getVirtualMachineExecutionId(),o.getVirtualMachineExecutionId());
	}
	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}
	public String getHostname() {
		return hostname;
	}
	public String getVirtualMachineIP() {
		return virtualMachineIP;
	}
	public void setVirtualMachineIP(String virtualMachineIP) {
		this.virtualMachineIP = virtualMachineIP;
	}
	public void setVmCores(int vmCores) {
		this.vmCores = vmCores;
	}
	public void setVmMemory(int vmMemory) {
		this.vmMemory = vmMemory;
	}
	public void setVirtualMachineNetMask(String virtualMachineNetMask) {
		this.virtualMachineNetMask = virtualMachineNetMask;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	
}
