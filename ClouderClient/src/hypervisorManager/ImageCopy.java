package hypervisorManager;

import java.io.File;
import java.io.Serializable;

import communication.ServerMessageSender;
import unacloudEnums.VirtualMachineExecutionStateEnum;
import utils.SystemUtils;
import virtualMachineConfiguration.AbstractVirtualMachineConfigurator;
import virtualMachineManager.PersistentExecutionManager;
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
				//configurator.setHypervisor(hypervisor);
				configurator.setExecution(machineExecution);
				//TODO Evaluar si hacerlo en el apagado porque es mas importante el tiempo de arranque.
				hypervisor.registerVirtualMachine(this);
    			hypervisor.restoreVirtualMachineSnapshot(this,"unacloudbase");
        		hypervisor.configureVirtualMachineHardware(machineExecution.getCores(),machineExecution.getMemory(),this);
    			hypervisor.startVirtualMachine(this);
    			SystemUtils.sleep(50000);
    			configurator.configureHostname();
    			configurator.configureIP();
    	        PersistentExecutionManager.startUpMachine(machineExecution,!configurator.doPostConfigure());
			}else ServerMessageSender.reportVirtualMachineState(machineExecution.getId(), VirtualMachineExecutionStateEnum.FAILED,"Invalid virtual machine configurator.");
		} catch (Exception e) {
			ServerMessageSender.reportVirtualMachineState(machineExecution.getId(), VirtualMachineExecutionStateEnum.FAILED,"Configurator class error: "+e.getMessage());
		}
	}
	public synchronized ImageCopy cloneCopy(ImageCopy dest){
		Hypervisor hypervisor=HypervisorFactory.getHypervisor(this.getImage().getHypervisorId());
		hypervisor.cloneVirtualMachine(this,dest);
		return dest;
	}
	public synchronized void init(){
		Hypervisor hypervisor=HypervisorFactory.getHypervisor(image.getHypervisorId());
		hypervisor.registerVirtualMachine(this);
		try {
			hypervisor.changeVirtualMachineMac(this);
			hypervisor.takeVirtualMachineSnapshot(this,"unacloudbase");
		} catch (HypervisorOperationException e) {
			e.printStackTrace();
		}
		hypervisor.unregisterVirtualMachine(this);
	}
	public void startVirtualMachine()throws HypervisorOperationException{
		Hypervisor hypervisor=HypervisorFactory.getHypervisor(this.getImage().getHypervisorId());
		hypervisor.startVirtualMachine(this);
	}
    
    public void executeCommandOnMachine( String command,String...args) throws HypervisorOperationException{
    	Hypervisor hypervisor=HypervisorFactory.getHypervisor(image.getHypervisorId());
    	hypervisor.executeCommandOnMachine(this,command,args);
    }

    public void copyFileOnVirtualMachine(String destinationRoute, File sourceFile) throws HypervisorOperationException{
    	Hypervisor hypervisor=HypervisorFactory.getHypervisor(image.getHypervisorId());
    	hypervisor.copyFileOnVirtualMachine(this,destinationRoute,sourceFile);
    }
    public void restartVirtualMachine() throws HypervisorOperationException{
    	Hypervisor hypervisor=HypervisorFactory.getHypervisor(this.getImage().getHypervisorId());
		hypervisor.restartVirtualMachine(this);
    }
    
    
    public synchronized void stopAndUnregister(){
    	Hypervisor hypervisor=HypervisorFactory.getHypervisor(image.getHypervisorId());
    	hypervisor.stopAndUnregister(this);
    }
}