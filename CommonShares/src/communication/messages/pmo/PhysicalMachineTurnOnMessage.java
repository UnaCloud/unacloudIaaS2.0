package communication.messages.pmo;

import communication.UnaCloudMessage;
import communication.messages.PhysicalMachineOperationMessage;

public class PhysicalMachineTurnOnMessage extends PhysicalMachineOperationMessage{
	private static final long serialVersionUID = -7026046062306316388L;
	String[] macs;
	public PhysicalMachineTurnOnMessage() {
		super(PM_TURN_ON);
	}
	@Override
	protected void processMessage(UnaCloudMessage message) {
		macs = message.getStrings(2, message.length);
	}
	public String[] getMacs() {
		return macs;
	}
}
