package communication.messages.pmo;

import communication.UnaCloudMessage;
import communication.messages.PhysicalMachineOperationMessage;

public class PhysicalMachineMonitorMessage extends PhysicalMachineOperationMessage{
	private static final long serialVersionUID = -6593319992803639653L;
	String operation;
	int monitorFrequency;
    int registerFrequency;
	public PhysicalMachineMonitorMessage() {
		super(PM_MONITOR);
	}
	@Override
	protected void processMessage(UnaCloudMessage message) {
		operation = message.getString(2);
		if(message.length>4){
			monitorFrequency = message.getInteger(3);
		    registerFrequency = message.getInteger(4);
		}
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
