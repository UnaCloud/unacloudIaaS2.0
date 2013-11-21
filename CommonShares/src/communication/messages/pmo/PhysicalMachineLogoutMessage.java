package communication.messages.pmo;

import communication.UnaCloudMessage;
import communication.messages.PhysicalMachineOperationMessage;

public class PhysicalMachineLogoutMessage extends PhysicalMachineOperationMessage{
	public PhysicalMachineLogoutMessage() {
		super(PM_LOGOUT);
	}
	@Override
	protected void processMessage(UnaCloudMessage message) {
		// TODO Auto-generated method stub
	}
}
