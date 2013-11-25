package virtualMachineConfiguration;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import communication.messages.vmo.VirtualMachineStartMessage;
import virtualMachineExecution.PersistentExecutionManager;
import virtualmachine.Hypervisor;
import virtualmachine.HypervisorFactory;
import virtualmachine.HypervisorOperationException;

public abstract class AbstractVirtualMachineConfigurator {
	private static Random r=new Random();
	Hypervisor hypervisor;
	VirtualMachineStartMessage startMessage;
	public final void setHypervisor(Hypervisor hypervisor) {
		this.hypervisor = hypervisor;
	}
	public void setStartMessage(VirtualMachineStartMessage startMessage) {
		this.startMessage = startMessage;
	}
	void copyFile(String destRoute,File sourceRoute)throws HypervisorOperationException{
		hypervisor.copyFileOnVirtualMachine(startMessage.getUsername(),startMessage.getPassword(), destRoute, sourceRoute);
	}
	void executeCommand(String command,String...args)throws HypervisorOperationException{
		hypervisor.executeCommandOnMachine(startMessage.getUsername(),startMessage.getPassword(), command, args);
	}
	public abstract void configureHostname() throws HypervisorOperationException;
	public abstract void configureIP() throws HypervisorOperationException;
    public abstract void configureDHCP() throws HypervisorOperationException;
    public abstract void configureHostTable() throws HypervisorOperationException;
    public abstract void doPostConfigure();
	public final void start(){
		try {
			hypervisor.changeVirtualMachineMac();
			hypervisor.startVirtualMachine();
		    configureHostname();
	        configureIP();
	        if(!startMessage.isPersistent()){
	        	hypervisor.stopVirtualMachine();
	            waitTime(10000);
	            //takeSnapshotOnMachine("base");
	        }else doPostConfigure();
	        PersistentExecutionManager.addExecution(startMessage);
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
	public File generateRandomFile(){
		return new File("temp/"+r.nextLong()+".txt");
	}
	
	public static void startVirtualMachine(VirtualMachineStartMessage startMessage){
		Hypervisor hypervisor=HypervisorFactory.getHypervisor(startMessage.getHypervisorName(),startMessage.getHypervisorPath(),startMessage.getVmPath());
		try {
			Class<?> configuratorClass=Class.forName("virtualMachineConfiguration."+startMessage.getConfiguratorClass());
			Object configuratorObject=configuratorClass.getConstructor().newInstance();
			if(configuratorObject instanceof AbstractVirtualMachineConfigurator){
				AbstractVirtualMachineConfigurator configurator=(AbstractVirtualMachineConfigurator)configuratorObject;
				configurator.setHypervisor(hypervisor);
				configurator.setStartMessage(startMessage);
				configurator.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

