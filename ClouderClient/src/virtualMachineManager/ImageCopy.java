package virtualMachineManager;

import java.io.File;
import java.io.Serializable;

public class ImageCopy implements Serializable{
	private static final long serialVersionUID = 8911955514393569155L;
	String virtualMachineName;
	File mainFile;
	Image image;
	transient VirtualMachineImageStatus status=VirtualMachineImageStatus.FREE;
	
	
	public File getMainFile() {
		return mainFile;
	}
	public void setMainFile(File mainFile) {
		this.mainFile = mainFile;
	}
	public String getVirtualMachineName() {
		return virtualMachineName;
	}
	public void setVirtualMachineName(String virtualMachineName) {
		this.virtualMachineName = virtualMachineName;
	}
	public VirtualMachineImageStatus getStatus() {
		return status;
	}
	public void setStatus(VirtualMachineImageStatus status) {
		this.status = status;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
}