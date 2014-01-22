package virtualMachineConfiguration;

import hypervisorManager.Hypervisor;
import hypervisorManager.HypervisorOperationException;

import java.io.File;
import java.util.Random;

import unacloudEnums.VirtualMachineExecutionStateEnum;
import virtualMachineManager.PersistentExecutionManager;
import virtualMachineManager.VirtualMachineExecution;

import communication.ServerMessageSender;

public abstract class AbstractVirtualMachineConfigurator{
	private static Random r=new Random();
	VirtualMachineExecution execution;
	public void setExecution(VirtualMachineExecution execution) {
		this.execution = execution;
	}
	void copyFile(String destRoute,File sourceRoute)throws HypervisorOperationException{
		hypervisor.copyFileOnVirtualMachine(execution.getImage(), destRoute, sourceRoute);
	}
	void executeCommand(String command,String...args)throws HypervisorOperationException{
		hypervisor.executeCommandOnMachine(execution.getImage(), command, args);
	}
    public void start() {
    	ServerMessageSender.reportVirtualMachineState(execution.getId(), VirtualMachineExecutionStateEnum.CONFIGURING, "Configuring virtual machine");
        try {
        	synchronized (execution.getImage()) {
        		hypervisor.registerVirtualMachine(execution.getImage());

    			//TODO Evaluar si hacerlo en el apagado porque es mas importante el tiempo de arranque.
    			hypervisor.restoreVirtualMachineSnapshot(execution.getImage(),"unacloudbase");
    			
        		hypervisor.configureVirtualMachineHardware(execution.getCores(),execution.getMemory(),execution.getImage());
    			hypervisor.startVirtualMachine(execution.getImage());
    			waitTime(50000);
    		    configureHostname();
    		    configureIP();
    	        PersistentExecutionManager.startUpMachine(execution,!doPostConfigure());
			}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public final void waitTime(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
        }
    }
	public File generateRandomFile(){
		if(!new File("temp").exists())new File("temp").mkdir();
		return new File("temp/"+Math.abs(r.nextLong())+".txt");
	}
	public abstract void configureHostname() throws HypervisorOperationException;
	public abstract void configureIP() throws HypervisorOperationException;
    public abstract void configureDHCP() throws HypervisorOperationException;
    public abstract void configureHostTable() throws HypervisorOperationException;
    /**
     * Returns true if the VM should be started again 
     * @return
     */
    public abstract boolean doPostConfigure();
}
