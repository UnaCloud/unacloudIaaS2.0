package virtualMachineConfiguration;

import hypervisorManager.Hypervisor;
import hypervisorManager.HypervisorFactory;
import hypervisorManager.HypervisorOperationException;

import java.io.File;
import java.util.Random;

import communication.ServerMessageSender;
import communication.messages.vmo.VirtualMachineStartResponse;
import unacloudEnums.VirtualMachineExecutionStateEnum;
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
    	ServerMessageSender.reportVirtualMachineState(execution.getId(), VirtualMachineExecutionStateEnum.CONFIGURING, "Configuring virtual machine");
        try {
    		hypervisor.registerVirtualMachine(execution.getImage());
    		if(!hypervisor.existsVirtualMachineSnapshot(execution.getImage(),"unacloudbase")){
    			//TODO También se puede tomar el snapshot luego de iniciar la máquina y ante de configurarla
    			hypervisor.takeVirtualMachineSnapshot(execution.getImage(),"unacloudbase");
    			hypervisor.changeVirtualMachineMac(execution.getImage());
    		}else{
    			//TODO Evaluar si hacerlo en el apagado porque es mas importante el tiempo de arranque.
    			hypervisor.restoreVirtualMachineSnapshot(execution.getImage(),"unacloudbase");
    		}
    		hypervisor.configureVirtualMachineHardware(execution.getCores(),execution.getMemory(),execution.getImage());
			hypervisor.startVirtualMachine(execution.getImage());
		    configureHostname();
		    configureIP();
	        /*if(!execution.isPersistent()){
	        	hypervisor.stopVirtualMachine(execution.getImage());
	            //takeSnapshotOnMachine("base");
	        }else*/ 
	        doPostConfigure();
	        PersistentExecutionManager.startUpMachine(execution);
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
	
	public static VirtualMachineStartResponse startVirtualMachine(VirtualMachineExecution machineExecution){
		System.out.println("startVirtualMachine");
		VirtualMachineImage image=VirtualMachineImageManager.getFreeImageCopy(machineExecution.getImageId());
		machineExecution.setImage(image);
		VirtualMachineStartResponse resp=new VirtualMachineStartResponse();
		Hypervisor hypervisor=HypervisorFactory.getHypervisor(image.getHypervisorId());
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
