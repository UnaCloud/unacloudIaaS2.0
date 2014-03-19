package communication.messages.ao;

import communication.messages.AgentMessage;

public class ClearImageFromCacheMessage extends AgentMessage{

	private static final long serialVersionUID = 524061116935661249L;

	public ClearImageFromCacheMessage(long imageId) {
		super(CLEAR_IMAGE_FROM_CACHE);
		
	}
}
