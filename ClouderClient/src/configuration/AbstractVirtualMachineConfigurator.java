package configuration;

import java.io.File;

import communication.messages.vmo.VirtualMachineStartMessage;
import virtualmachine.Hypervisor;
import virtualmachine.HypervisorOperationException;

public abstract class AbstractVirtualMachineConfigurator {

	Hypervisor hypervisor;
	VirtualMachineStartMessage startMessage;
	public AbstractVirtualMachineConfigurator(Hypervisor hypervisor) {
		this.hypervisor = hypervisor;
	}
	public void changeMac(){
		
	}
	void copyFile(String destRoute,File sourceRoute)throws HypervisorOperationException{
		hypervisor.copyFileOnVirtualMachine(startMessage.getUsername(),startMessage.getPassword(), destRoute, sourceRoute);
	}
	void executeCommand(String command,String...args)throws HypervisorOperationException{
		hypervisor.executeCommandOnMachine(startMessage.getUsername(),startMessage.getPassword(), command, args);
	}
	public abstract void configureHostname();
	public abstract void configureIP(String netmask, String ip);
    public abstract void configureDHCP();
    public abstract void doPostConfigure();
	public final void startVirtualMachine(){
		try {
			
			hypervisor.changeVirtualMachineMac();
			hypervisor.startVirtualMachine();
		    configureHostname();
	        configureIP(startMessage.getVirtualMachineNetMask(),startMessage.getVmIP());
	        if(!startMessage.isPersistent()){
	        	hypervisor.stopVirtualMachine();
	            waitTime(10000);
	            //takeSnapshotOnMachine("base");
	        }else doPostConfigure();
		} catch (HypervisorOperationException e) {
			e.printStackTrace();
		}
	}
	public final void waitTime(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
        }
    }
}

