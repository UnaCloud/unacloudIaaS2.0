
package back.deployers

import java.util.concurrent.TimeUnit;

import grails.util.Environment;
import javassist.bytecode.stackmap.BasicBlock.Catch;
import back.services.VariableManagerService;

import com.losandes.utils.Constants;
import com.losandes.utils.Time;

import communication.messages.vmo.VirtualMachineStartMessage;
import communication.messages.vmo.VirtualMachineStopMessage;
import unacloud2.*;
import unacloudEnums.VirtualMachineExecutionStateEnum;

class DeployerService {
	
	//-----------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------
	
	
	/**
	 * Representation of server variable manager service
	 */
	
	VariableManagerService variableManagerService
	
	static transactional = false
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------
	
	
	/**
	 * Deploys a complete deployment
	 * @param deployment 
	 */
	
	def	deploy(Deployment deployment){

		deployVMs(deployment.cluster);

	}

	/**
	 * Deploys only new instances. This method is called with the add instance
	 * function 
	 * @param image Image with the new instances 
	 */
	
	def	deployNewInstances(DeployedImage image){
		
		VirtualMachineStartMessage vmsm=new VirtualMachineStartMessage();
		println "Deploying Image ----->" +image.image.name+" "+image.virtualMachines.size()
		/*
		 * Iterates all VMs in the image selecting only those which 
		 * are not deployed. This is verified thanks to the VM message
		 */
		image.virtualMachines.eachWithIndex() { vm, i ->
			
			
			if(vm.message.equals("Adding instance")&&vm.status== VirtualMachineExecutionStateEnum.DEPLOYING){
				println vm.name+" "+vm.message
				/*
				 * creates a message in order to start the machine
				 */
				
				vm.setMessage("Initializing")
				vm.save()
				println vm.name+" "+vm.message
				if(!Environment.isDevelopmentMode()){
					try{

						vmsm.setExecutionTime(new Time(vm.runningTimeInHours(), TimeUnit.HOURS))
						vmsm.setHostname(vm.name)
						vmsm.setVirtualMachineIP(vm.ip.ip)
						vmsm.setVirtualMachineNetMask(vm.ip.ipPool.mask)
						vmsm.setVmCores(vm.hardwareProfile.cores)
						vmsm.setVmMemory(vm.hardwareProfile.ram)
						vmsm.setVirtualMachineExecutionId(vm.id)
						vmsm.setVirtualMachineImageId(image.image.id)
						String pmIp=vm.executionNode.ip.ip;
						try{
						/*
						 * Sends the message to the physical machine agent
						 * where the virtual machine was allocated
						 */
							println "Abriendo socket a "+pmIp+" "+variableManagerService.getIntValue("CLOUDER_CLIENT_PORT");
							Socket s=new Socket(pmIp,variableManagerService.getIntValue("CLOUDER_CLIENT_PORT"));
							s.setSoTimeout(15000);
							ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
							oos.writeObject(vmsm);
							oos.flush();
							ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
							Object c=ois.readObject();
							oos.close();
							s.close();
						}catch(Exception e){
							vm.setStatus(VirtualMachineExecutionStateEnum.FAILED)
							vm.setMessage("Connection error")
							println e.getMessage()+" "+pmIp;
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Deploys all VMs in the cluster. 
	 * @param cluster Deployed cluster with the virtual machines
	 * @return
	 */
	def deployVMs(DeployedCluster cluster){
		/*
		 * Iterates over all virtual machines
		 */
		for(image in cluster.images) {
			VirtualMachineStartMessage vmsm=new VirtualMachineStartMessage();
			println "Deploying Image ----->" +image.image.name+" "+image.virtualMachines.size()

			image.virtualMachines.eachWithIndex() { vm, i ->
				try{
					
					/*
					 * creates a message in order to start the machine
					 */
					println vm.runningTimeInHours()
					vmsm.setExecutionTime(new Time(vm.runningTimeInHours(), TimeUnit.HOURS))
					vmsm.setHostname(vm.name)
					vmsm.setVirtualMachineIP(vm.ip.ip)
					println "vmsm.setVirtualMachineIP --->"+ vm.ip.ip
					vmsm.setVirtualMachineNetMask(vm.ip.ipPool.mask)
					vmsm.setVmCores(vm.hardwareProfile.cores)
					vmsm.setVmMemory(vm.hardwareProfile.ram)
					vmsm.setVirtualMachineExecutionId(vm.id)
					vmsm.setVirtualMachineImageId(image.image.id)
					String pmIp=vm.executionNode.ip.ip;

					try{
						/*
						 * Sends the message to the physical machine agent
						 * where the virtual machine was allocated
						 */
						
						println "Abriendo socket a "+pmIp+" "+variableManagerService.getIntValue("CLOUDER_CLIENT_PORT");
						Socket s=new Socket(pmIp,variableManagerService.getIntValue("CLOUDER_CLIENT_PORT"));
						s.setSoTimeout(15000);
						ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
						oos.writeObject(vmsm);
						oos.flush();
						ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
						Object c=ois.readObject();
						oos.close();
						s.close();
					}catch(Exception e){
						vm.setMessage("Connection error")
						println e.getMessage()+" "+pmIp;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Stops a single virtual machine. Creates an stop message and sends it 
	 * to the physical machine agent where the virtual machine was allocated
	 * @param vm virtual machine to be stopped
	 */
	
	def stopVirtualMachine(VirtualMachineExecution vm){
		VirtualMachineStopMessage vmsm=new VirtualMachineStopMessage();
		vmsm.setVirtualMachineExecutionId(vm.id)
		String pmIp=vm.executionNode.ip.ip;
		try{
			Socket s=new Socket(pmIp,81);
			s.setSoTimeout(15000);
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			oos.writeObject(vmsm);
			oos.flush();
			Object c=ois.readObject();
			oos.close();
			s.close();
		}catch(Exception e){
			println e.getMessage()+" "+pmIp;
		}
	}
}
