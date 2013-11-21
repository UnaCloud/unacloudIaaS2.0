package communication.messages.pmo;

import communication.UnaCloudMessage;
import communication.messages.PhysicalMachineOperationMessage;

public class PhysicalMachineRestartMessage extends PhysicalMachineOperationMessage{
	public PhysicalMachineRestartMessage() {
		super(PM_RESTART);
	}
	@Override
	protected void processMessage(UnaCloudMessage message) {
		// TODO Auto-generated method stub
	}
}
