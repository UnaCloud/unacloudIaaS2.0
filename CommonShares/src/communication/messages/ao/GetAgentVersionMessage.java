package communication.messages.ao;

import communication.UnaCloudMessage;
import communication.messages.AgentMessage;

public class GetAgentVersionMessage extends AgentMessage{

	public GetAgentVersionMessage() {
		super(GET_VERSION);
	}
	@Override
	protected void processMessage(UnaCloudMessage message) {
		// TODO Auto-generated method stub
	}
}
