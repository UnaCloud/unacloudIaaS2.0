package communication.messages.vmo;

import communication.messages.VirtualMachineOperationMessage;

public class VirtualMachineStartMessage extends VirtualMachineOperationMessage implements Comparable<VirtualMachineStartMessage>{
	private static final long serialVersionUID = -5116988985857543662L;
	int hypervisorName,vmCores,vmMemory;
    String vmPath,hypervisorPath;
    int executionTime;
    String virtualMachineIP,virtualMachineNetMask,snapshotRoute;
    boolean persistent;
    long shutdownTime;
    
    String username,password,hostname;
    String configuratorClass;
    public VirtualMachineStartMessage() {
		super(VM_START);
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
	public String getHostname() {
		return hostname;
	}
	public String getConfiguratorClass() {
		return configuratorClass;
	}
	public String getVirtualMachineIP() {
		return virtualMachineIP;
	}
	public void setVirtualMachineIP(String virtualMachineIP) {
		this.virtualMachineIP = virtualMachineIP;
	}
	public void setHypervisorName(int hypervisorName) {
		this.hypervisorName = hypervisorName;
	}
	public void setVmCores(int vmCores) {
		this.vmCores = vmCores;
	}
	public void setVmMemory(int vmMemory) {
		this.vmMemory = vmMemory;
	}
	public void setVmPath(String vmPath) {
		this.vmPath = vmPath;
	}
	public void setHypervisorPath(String hypervisorPath) {
		this.hypervisorPath = hypervisorPath;
	}
	public void setVirtualMachineNetMask(String virtualMachineNetMask) {
		this.virtualMachineNetMask = virtualMachineNetMask;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public void setConfiguratorClass(String configuratorClass) {
		this.configuratorClass = configuratorClass;
	}
	
}
