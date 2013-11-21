package communication.messages.pmo;

import communication.UnaCloudMessage;
import communication.messages.PhysicalMachineOperationMessage;

public class PhysicalMachineTurnOffMessage extends PhysicalMachineOperationMessage{
	public PhysicalMachineTurnOffMessage() {
		super(PM_TURN_OFF);
	}
	@Override
	protected void processMessage(UnaCloudMessage message) {
		// TODO Auto-generated method stub
	}
}
