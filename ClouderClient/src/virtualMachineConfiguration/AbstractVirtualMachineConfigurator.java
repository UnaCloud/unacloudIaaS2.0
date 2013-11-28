package virtualMachineConfiguration;

import hypervisorManager.Hypervisor;
import hypervisorManager.HypervisorFactory;
import hypervisorManager.HypervisorOperationException;

import java.io.File;
import java.util.Random;

import communication.messages.vmo.VirtualMachineStartResponse;
import virtualMachineManager.PersistentExecutionManager;
import virtualMachineManager.VirtualMachineExecution;
import virtualMachineManager.VirtualMachineImage;
import virtualMachineManager.VirtualMachineImageManager;

public abstract class AbstractVirtualMachineConfigurator extends Thread{
	private static Random r=new Random();
	Hypervisor hypervisor;
	VirtualMachineExecution execution;
	public final void setHypervisor(Hypervisor hypervisor) {
		this.hypervisor = hypervisor;
	}
	public void setExecution(VirtualMachineExecution execution) {
		this.execution = execution;
	}
	void copyFile(String destRoute,File sourceRoute)throws HypervisorOperationException{
		hypervisor.copyFileOnVirtualMachine(execution.getImage(), destRoute, sourceRoute);
	}
	void executeCommand(String command,String...args)throws HypervisorOperationException{
		hypervisor.executeCommandOnMachine(execution.getImage(), command, args);
	}
	public abstract void configureHostname() throws HypervisorOperationException;
	public abstract void configureIP() throws HypervisorOperationException;
    public abstract void configureDHCP() throws HypervisorOperationException;
    public abstract void configureHostTable() throws HypervisorOperationException;
    public abstract void doPostConfigure();
    @Override
    public void run() {
    	try {
    		System.out.println("hypervisor.changeVirtualMachineMac");
			hypervisor.changeVirtualMachineMac(execution.getImage());
			System.out.println("hypervisor.startVirtualMachine");
			hypervisor.preconfigureAndStartVirtualMachine(execution);
			System.out.println("configureHostname");
		    configureHostname();
		    System.out.println("configureIP");
	        configureIP();
	        if(!execution.isPersistent()){
	        	hypervisor.stopVirtualMachine(execution.getImage());
	            waitTime(10000);
	            //takeSnapshotOnMachine("base");
	        }else doPostConfigure();
	        System.out.println("PersistentExecutionManager.addExecution");
	        PersistentExecutionManager.startUpMachine(execution);
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
		if(!new File("temp").exists())new File("temp").mkdir();
		return new File("temp/"+r.nextLong()+".txt");
	}
	
	public static VirtualMachineStartResponse startVirtualMachine(VirtualMachineExecution machineExecution){
		System.out.println("startVirtualMachine");
		VirtualMachineImage image=VirtualMachineImageManager.getFreeImageCopy(machineExecution.getImageId());
		VirtualMachineStartResponse resp=new VirtualMachineStartResponse();
		Hypervisor hypervisor=HypervisorFactory.getHypervisor(image.getVirtualMachineName());
		try {
			Class<?> configuratorClass=Class.forName("virtualMachineConfiguration."+image.getConfiguratorClass());
			Object configuratorObject=configuratorClass.getConstructor().newInstance();
			if(configuratorObject instanceof AbstractVirtualMachineConfigurator){
				AbstractVirtualMachineConfigurator configurator=(AbstractVirtualMachineConfigurator)configuratorObject;
				configurator.setHypervisor(hypervisor);
				configurator.setExecution(machineExecution);
				configurator.start();
				resp.setState(VirtualMachineStartResponse.VirtualMachineState.STARTING);
				resp.setMessage("Starting virtual machine...");
			}else{
				resp.setState(VirtualMachineStartResponse.VirtualMachineState.FAILED);
				resp.setMessage("Invalid virtual machine configurator.");
			}
			
		} catch (Exception e) {
			resp.setState(VirtualMachineStartResponse.VirtualMachineState.FAILED);
			resp.setMessage("Configurator class error: "+e.getMessage());
		}
		System.out.println(resp);
		return resp;
	}
}
