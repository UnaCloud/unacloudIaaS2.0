package communication.messages.ao;

import communication.UnaCloudMessage;
import communication.messages.AgentMessage;

public class UpdateAgentMessage extends AgentMessage{

	public UpdateAgentMessage() {
		super(UPDATE_OPERATION);
	}
	@Override
	protected void processMessage(UnaCloudMessage message) {
		// TODO Auto-generated method stub
	}
}
