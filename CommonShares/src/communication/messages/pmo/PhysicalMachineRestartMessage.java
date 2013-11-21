package communication.messages.pmo;

import communication.UnaCloudMessage;
import communication.messages.PhysicalMachineOperationMessage;

public class PhysicalMachineRestartMessage extends PhysicalMachineOperationMessage{
	private static final long serialVersionUID = -1777920929881348888L;
	public PhysicalMachineRestartMessage() {
		super(PM_RESTART);
	}
	@Override
	protected void processMessage(UnaCloudMessage message) {
		// TODO Auto-generated method stub
	}
}
