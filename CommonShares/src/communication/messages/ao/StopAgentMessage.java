package communication.messages.ao;

import communication.UnaCloudMessage;
import communication.messages.AgentMessage;

public class StopAgentMessage extends AgentMessage{

	public StopAgentMessage() {
		super(STOP_CLIENT);
	}
	@Override
	protected void processMessage(UnaCloudMessage message) {
		// TODO Auto-generated method stub
	}
}
