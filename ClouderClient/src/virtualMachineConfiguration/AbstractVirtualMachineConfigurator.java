package virtualMachineConfiguration;

import hypervisorManager.HypervisorOperationException;

import java.io.File;
import java.util.Random;

import virtualMachineManager.VirtualMachineExecution;

public abstract class AbstractVirtualMachineConfigurator{
	private static Random r=new Random();
	VirtualMachineExecution execution;
	public void setExecution(VirtualMachineExecution execution) {
		this.execution = execution;
	}
	public static File generateRandomFile(){
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
    public final void waitTime(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
        }
    }
}
