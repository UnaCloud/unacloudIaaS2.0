package communication.messages.ao;

import communication.UnaCloudMessage;
import communication.messages.AgentMessage;

public class UpdateAgentMessage extends AgentMessage{

	private static final long serialVersionUID = -3358107648144467395L;
	public UpdateAgentMessage() {
		super(UPDATE_OPERATION);
	}
	@Override
	protected void processMessage(UnaCloudMessage message) {
		// TODO Auto-generated method stub
	}
}
