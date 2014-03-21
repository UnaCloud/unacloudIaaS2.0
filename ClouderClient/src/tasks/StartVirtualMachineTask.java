package tasks;

import java.util.Date;

import hypervisorManager.ImageCopy;
import virtualMachineManager.ImageCacheManager;
import virtualMachineManager.VirtualMachineExecution;

public class StartVirtualMachineTask implements Runnable{
	VirtualMachineExecution machineExecution;
	public StartVirtualMachineTask(VirtualMachineExecution machineExecution) {
		this.machineExecution = machineExecution;
	}
	@Override
	public void run() {
		System.out.println("startStart+VirtualMachine on "+new Date());
		ImageCopy image=ImageCacheManager.getFreeImageCopy(machineExecution.getImageId());
		System.out.println("obntuve imagen on "+new Date());
		machineExecution.setImage(image);
		image.configureAndStart(machineExecution);
		System.out.println("endStartVirtualMachine on "+new Date());
	}
}
