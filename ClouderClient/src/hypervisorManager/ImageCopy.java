package hypervisorManager;

import java.io.File;
import java.io.Serializable;

import communication.ServerMessageSender;
import unacloudEnums.VirtualMachineExecutionStateEnum;
import utils.RandomUtils;
import virtualMachineConfiguration.AbstractVirtualMachineConfigurator;
import virtualMachineManager.VirtualMachineExecution;
import virtualMachineManager.VirtualMachineImageStatus;

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
	public synchronized void configureAndStart(VirtualMachineExecution machineExecution){
		Hypervisor hypervisor=HypervisorFactory.getHypervisor(getImage().getHypervisorId());
		try {
			Class<?> configuratorClass=Class.forName("virtualMachineConfiguration."+getImage().getConfiguratorClass());
			Object configuratorObject=configuratorClass.getConstructor().newInstance();
			if(configuratorObject instanceof AbstractVirtualMachineConfigurator){
				AbstractVirtualMachineConfigurator configurator=(AbstractVirtualMachineConfigurator)configuratorObject;
				configurator.setHypervisor(hypervisor);
				configurator.setExecution(machineExecution);
				configurator.start();
			}else ServerMessageSender.reportVirtualMachineState(machineExecution.getId(), VirtualMachineExecutionStateEnum.FAILED,"Invalid virtual machine configurator.");
		} catch (Exception e) {
			ServerMessageSender.reportVirtualMachineState(machineExecution.getId(), VirtualMachineExecutionStateEnum.FAILED,"Configurator class error: "+e.getMessage());
		}
	}
	public synchronized ImageCopy cloneCopy(String machineRepositoryDestination){
		ImageCopy dest=new ImageCopy();
		final String vmName="v"+RandomUtils.generateRandomString(9);
		File root=new File(machineRepositoryDestination+"\\"+getImage().getId()+"\\"+vmName);
		dest.setMainFile(new File(root,vmName+"."+getMainFile().getName().split("\\.")[1]));
		dest.setStatus(VirtualMachineImageStatus.LOCK);
		dest.setVirtualMachineName(vmName);
		Hypervisor hypervisor=HypervisorFactory.getHypervisor(this.getImage().getHypervisorId());
		hypervisor.cloneVirtualMachine(this,dest);
		return dest;
	}
}