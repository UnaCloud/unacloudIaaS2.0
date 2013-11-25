package communication.messages.pmo;

import communication.messages.PhysicalMachineOperationMessage;

public class PhysicalMachineMonitorMessage extends PhysicalMachineOperationMessage{
	private static final long serialVersionUID = -6593319992803639653L;
	String operation;
	int monitorFrequency;
    int registerFrequency;
	public PhysicalMachineMonitorMessage() {
		super(PM_MONITOR);
	}
	public String getOperation() {
		return operation;
	}
	public int getMonitorFrequency() {
		return monitorFrequency;
	}
	public int getRegisterFrequency() {
		return registerFrequency;
	}
}
