package tasks;

import java.util.Date;

import communication.ServerMessageSender;

import Exceptions.VirtualMachineExecutionException;
import hypervisorManager.ImageCopy;
import unacloudEnums.VirtualMachineExecutionStateEnum;
import virtualMachineManager.ImageCacheManager;
import virtualMachineManager.VirtualMachineExecution;

public class StartVirtualMachineTask implements Runnable{
	VirtualMachineExecution machineExecution;
	/**
	 * class constructor
	 * @param machineExecution VM instance to be started
	 */
	public StartVirtualMachineTask(VirtualMachineExecution machineExecution) {
		this.machineExecution = machineExecution;
	}
	
	/**
	 * Executes start machine task
	 */
	@Override
	public void run() {
		System.out.println("startStart+VirtualMachine on "+new Date());
		try{
			ImageCopy image=ImageCacheManager.getFreeImageCopy(machineExecution.getImageId());
			System.out.println("obntuve imagen on "+new Date());
			machineExecution.setImage(image);
			image.configureAndStart(machineExecution);
			System.out.println("endStartVirtualMachine on "+new Date());
		}catch(VirtualMachineExecutionException ex){
			ServerMessageSender.reportVirtualMachineState(machineExecution.getId(), VirtualMachineExecutionStateEnum.FAILED,ex.getMessage());
		}
		
	}
}
