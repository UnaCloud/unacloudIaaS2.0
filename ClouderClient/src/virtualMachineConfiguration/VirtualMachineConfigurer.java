package virtualMachineConfiguration;

import hypervisorManager.Hypervisor;
import hypervisorManager.HypervisorFactory;
import hypervisorManager.ImageCopy;
import communication.ServerMessageSender;
import communication.messages.vmo.VirtualMachineStartResponse;
import unacloudEnums.VirtualMachineExecutionStateEnum;
import virtualMachineManager.VirtualMachineExecution;
import virtualMachineManager.ImageCacheManager;

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
		ImageCopy image=ImageCacheManager.getFreeImageCopy(machineExecution.getImageId());
		machineExecution.setImage(image);
		image.configureAndStart(machineExecution);
	}
}
