package back.deployers

import grails.util.Environment;
import javassist.bytecode.stackmap.BasicBlock.Catch;
import back.services.VariableManagerService;

import com.losandes.utils.Constants;

import communication.messages.vmo.VirtualMachineStartMessage;
import communication.messages.vmo.VirtualMachineStopMessage;
import unacloud2.*;
import unacloudEnums.VirtualMachineExecutionStateEnum;

class DeployerService {

	TransferService transferService

	VariableManagerService variableManagerService

	static transactional = false

	def	deploy(Deployment deployment){

		println "llamando"
		//transferService.transferVM(deployment.cluster)
		println "llamé"
		deployVMs(deployment.cluster);

	}

	def	deployNewInstances(DeployedImage image){

		VirtualMachineStartMessage vmsm=new VirtualMachineStartMessage();
		println "Deploying Image ----->" +image.image.name+" "+image.virtualMachines.size()

		image.virtualMachines.eachWithIndex() { vm, i ->
			println vm.name+" "+vm.message
			if(vm.message.equals("Adding instance")&&vm.status== VirtualMachineExecutionStateEnum.DEPLOYING){
				vm.setMessage("Initializing")
				vm.save()
				println vm.name+" "+vm.message
				if(!Environment.isDevelopmentMode()){
					try{

						vmsm.setExecutionTime(vm.runningTimeInHours())
						vmsm.setHostname(vm.name)
						vmsm.setVirtualMachineIP(vm.ip.ip)
						vmsm.setVirtualMachineNetMask(vm.ip.ipPool.mask)
						vmsm.setVmCores(vm.cores)
						vmsm.setVmMemory(vm.ram)
						vmsm.setVirtualMachineExecutionId(vm.id)
						vmsm.setVirtualMachineImageId(image.image.id)
						String pmIp=vm.executionNode.ip.ip;
						try{
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
	}

	def deployVMs(DeployedCluster cluster){
		for(image in cluster.images) {
			VirtualMachineStartMessage vmsm=new VirtualMachineStartMessage();
			println "Deploying Image ----->" +image.image.name+" "+image.virtualMachines.size()

			image.virtualMachines.eachWithIndex() { vm, i ->
				try{
					vmsm.setExecutionTime(vm.runningTimeInHours())
					vmsm.setHostname(vm.name)
					vmsm.setVirtualMachineIP(vm.ip.ip)
					println "vmsm.setVirtualMachineIP --->"+ vm.ip.ip
					vmsm.setVirtualMachineNetMask(vm.ip.ipPool.mask)
					vmsm.setVmCores(vm.cores)
					vmsm.setVmMemory(vm.ram)
					vmsm.setVirtualMachineExecutionId(vm.id)
					vmsm.setVirtualMachineImageId(image.image.id)
					String pmIp=vm.executionNode.ip.ip;

					try{
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
