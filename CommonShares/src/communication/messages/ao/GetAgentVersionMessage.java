package communication.messages.ao;

import communication.UnaCloudMessage;
import communication.messages.AgentMessage;

public class GetAgentVersionMessage extends AgentMessage{

	private static final long serialVersionUID = -4156099004206342347L;
	public GetAgentVersionMessage() {
		super(GET_VERSION);
	}
	@Override
	protected void processMessage(UnaCloudMessage message) {
		// TODO Auto-generated method stub
	}
}
