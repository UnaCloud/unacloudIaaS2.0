package virtualMachineConfiguration;

import hypervisorManager.Hypervisor;
import hypervisorManager.HypervisorFactory;
import communication.ServerMessageSender;
import communication.messages.vmo.VirtualMachineStartResponse;
import unacloudEnums.VirtualMachineExecutionStateEnum;
import virtualMachineManager.VirtualMachineExecution;
import virtualMachineManager.VirtualMachineImage;
import virtualMachineManager.VirtualMachineImageManager;

public final class VirtualMachineConfigurer extends Thread{
	VirtualMachineExecution machineExecution;
	public VirtualMachineConfigurer(VirtualMachineExecution machineExecution) {
		this.machineExecution = machineExecution;
	}
	public VirtualMachineStartResponse startProcess(){
		VirtualMachineStartResponse resp=new VirtualMachineStartResponse();
		resp.setState(VirtualMachineStartResponse.VirtualMachineState.STARTING);
		resp.setMessage("Starting virtual machine...");
		start();
		return resp;
	}
	@Override
	public void run() {
		System.out.println("startVirtualMachine");
		VirtualMachineImage image=VirtualMachineImageManager.getFreeImageCopy(machineExecution.getImageId());
		machineExecution.setImage(image);
		Hypervisor hypervisor=HypervisorFactory.getHypervisor(image.getHypervisorId());
		try {
			Class<?> configuratorClass=Class.forName("virtualMachineConfiguration."+image.getConfiguratorClass());
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
}