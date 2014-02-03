
package unacloudws.responses;

import java.util.Calendar;


public class VirtualMachineExecutionWS {

    protected String systemUser;
    protected Integer template;
    protected Integer virtualMachine;
    protected String virtualMachineExecutionCode;
    protected int virtualMachineExecutionCores;
    protected long virtualMachineExecutionHardDisk;
    protected String virtualMachineExecutionIP;
    protected int virtualMachineExecutionRAMMemory;
    protected Calendar virtualMachineExecutionStart;
    protected int virtualMachineExecutionStatus;
    protected String virtualMachineExecutionStatusMessage;
    protected Calendar virtualMachineExecutionStop;
    protected String virtualMachineName;
	public String getSystemUser() {
		return systemUser;
	}
	public void setSystemUser(String systemUser) {
		this.systemUser = systemUser;
	}
	public Integer getTemplate() {
		return template;
	}
	public void setTemplate(Integer template) {
		this.template = template;
	}
	public Integer getVirtualMachine() {
		return virtualMachine;
	}
	public void setVirtualMachine(Integer virtualMachine) {
		this.virtualMachine = virtualMachine;
	}
	public String getVirtualMachineExecutionCode() {
		return virtualMachineExecutionCode;
	}
	public void setVirtualMachineExecutionCode(String virtualMachineExecutionCode) {
		this.virtualMachineExecutionCode = virtualMachineExecutionCode;
	}
	public int getVirtualMachineExecutionCores() {
		return virtualMachineExecutionCores;
	}
	public void setVirtualMachineExecutionCores(int virtualMachineExecutionCores) {
		this.virtualMachineExecutionCores = virtualMachineExecutionCores;
	}
	public long getVirtualMachineExecutionHardDisk() {
		return virtualMachineExecutionHardDisk;
	}
	public void setVirtualMachineExecutionHardDisk(
			long virtualMachineExecutionHardDisk) {
		this.virtualMachineExecutionHardDisk = virtualMachineExecutionHardDisk;
	}
	public String getVirtualMachineExecutionIP() {
		return virtualMachineExecutionIP;
	}
	public void setVirtualMachineExecutionIP(String virtualMachineExecutionIP) {
		this.virtualMachineExecutionIP = virtualMachineExecutionIP;
	}
	public int getVirtualMachineExecutionRAMMemory() {
		return virtualMachineExecutionRAMMemory;
	}
	public void setVirtualMachineExecutionRAMMemory(
			int virtualMachineExecutionRAMMemory) {
		this.virtualMachineExecutionRAMMemory = virtualMachineExecutionRAMMemory;
	}
	public Calendar getVirtualMachineExecutionStart() {
		return virtualMachineExecutionStart;
	}
	public void setVirtualMachineExecutionStart(
			Calendar virtualMachineExecutionStart) {
		this.virtualMachineExecutionStart = virtualMachineExecutionStart;
	}
	public int getVirtualMachineExecutionStatus() {
		return virtualMachineExecutionStatus;
	}
	public void setVirtualMachineExecutionStatus(int virtualMachineExecutionStatus) {
		this.virtualMachineExecutionStatus = virtualMachineExecutionStatus;
	}
	public String getVirtualMachineExecutionStatusMessage() {
		return virtualMachineExecutionStatusMessage;
	}
	public void setVirtualMachineExecutionStatusMessage(
			String virtualMachineExecutionStatusMessage) {
		this.virtualMachineExecutionStatusMessage = virtualMachineExecutionStatusMessage;
	}
	public Calendar getVirtualMachineExecutionStop() {
		return virtualMachineExecutionStop;
	}
	public void setVirtualMachineExecutionStop(
			Calendar virtualMachineExecutionStop) {
		this.virtualMachineExecutionStop = virtualMachineExecutionStop;
	}
	public String getVirtualMachineName() {
		return virtualMachineName;
	}
	public void setVirtualMachineName(String virtualMachineName) {
		this.virtualMachineName = virtualMachineName;
	}
}
