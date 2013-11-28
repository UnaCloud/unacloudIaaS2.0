package virtualMachineConfiguration;

import java.io.File;
import java.util.Random;

import communication.messages.vmo.VirtualMachineStartMessage;
import communication.messages.vmo.VirtualMachineStartResponse;
import virtualMachineExecution.PersistentExecutionManager;
import virtualMachineManager.VirtualMachineImage;
import virtualMachineManager.VirtualMachineImageManager;
import virtualmachine.Hypervisor;
import virtualmachine.HypervisorFactory;
import virtualmachine.HypervisorOperationException;

public abstract class AbstractVirtualMachineConfigurator extends Thread{
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
    @Override
    public void run() {
    	try {
    		System.out.println("hypervisor.changeVirtualMachineMac");
			hypervisor.changeVirtualMachineMac();
			System.out.println("hypervisor.startVirtualMachine");
			hypervisor.startVirtualMachine();
			System.out.println("configureHostname");
		    configureHostname();
		    System.out.println("configureIP");
	        configureIP();
	        if(!startMessage.isPersistent()){
	        	hypervisor.stopVirtualMachine();
	            waitTime(10000);
	            //takeSnapshotOnMachine("base");
	        }else doPostConfigure();
	        System.out.println("PersistentExecutionManager.addExecution");
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
		if(!new File("temp").exists())new File("temp").mkdir();
		return new File("temp/"+r.nextLong()+".txt");
	}
	
	public static VirtualMachineStartResponse startVirtualMachine(VirtualMachineStartMessage startMessage){
		System.out.println("startVirtualMachine");
		VirtualMachineImage vmi=VirtualMachineImageManager.getImage(startMessage.getVirtualMachineId());
		VirtualMachineStartResponse resp=new VirtualMachineStartResponse();
		Hypervisor hypervisor=HypervisorFactory.getHypervisor(startMessage.getHypervisorName(),startMessage.getHypervisorPath(),startMessage.getVmPath());
		if(hypervisor==null){
			resp.setState(VirtualMachineStartResponse.VirtualMachineState.FAILED);
			resp.setMessage("Hypervisor not found name:"+startMessage.getHypervisorName()+" path:"+startMessage.getHypervisorPath());
		}else{
			try {
				Class<?> configuratorClass=Class.forName("virtualMachineConfiguration."+startMessage.getConfiguratorClass());
				Object configuratorObject=configuratorClass.getConstructor().newInstance();
				if(configuratorObject instanceof AbstractVirtualMachineConfigurator){
					AbstractVirtualMachineConfigurator configurator=(AbstractVirtualMachineConfigurator)configuratorObject;
					configurator.setHypervisor(hypervisor);
					configurator.setStartMessage(startMessage);
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
		}
		System.out.println(resp);
		return resp;
	}
}
