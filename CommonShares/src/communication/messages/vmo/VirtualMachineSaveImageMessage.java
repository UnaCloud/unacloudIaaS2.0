package communication.messages.vmo;

import communication.messages.VirtualMachineOperationMessage;

public class VirtualMachineSaveImageMessage extends VirtualMachineOperationMessage{

	private long imageId, virtualMachineId;
	private static final long serialVersionUID = 3147489071041260127L;
	
	public VirtualMachineSaveImageMessage() {
		super(VM_SAVE_IMG);
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public long getVirtualMachineId() {
		return virtualMachineId;
	}

	public void setVirtualMachineId(long virtualMachineId) {
		this.virtualMachineId = virtualMachineId;
	}
		
}