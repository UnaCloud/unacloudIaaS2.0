package tasks;

import communication.messages.vmo.VirtualMachineStopMessage;

import virtualMachineManager.PersistentExecutionManager;

public class StopVirtualMachineTask implements Runnable{
	VirtualMachineStopMessage stopMessage;
	public StopVirtualMachineTask(VirtualMachineStopMessage stopMessage) {
		this.stopMessage = stopMessage;
	}
	@Override
	public void run() {
		PersistentExecutionManager.removeExecution((stopMessage).getVirtualMachineExecutionId(),false);
	}
}
