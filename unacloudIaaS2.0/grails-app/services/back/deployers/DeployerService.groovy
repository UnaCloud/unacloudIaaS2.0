package back.deployers

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
		println "llam�"
		deployVMs(deployment.cluster);

	}

	def	deployNewInstances(DeployedImage image){

		VirtualMachineStartMessage vmsm=new VirtualMachineStartMessage();
		println "Deploying Image ----->" +image.image.name+" "+image.virtualMachines.size()

		image.virtualMachines.eachWithIndex() { vm, i ->
			if(vm.message.equals("Adding instance")&&vm.status== VirtualMachineExecutionStateEnum.DEPLOYING){
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
				println vmsm
				try{
					println "Abriendo socket a "+pmIp+" "+variableManagerService.getIntValue("CLOUDER_CLIENT_PORT");
					Socket s=new Socket(pmIp,variableManagerService.getIntValue("CLOUDER_CLIENT_PORT"));
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(vmsm);
					oos.flush();
					ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
					Object c=ois.readObject();
					oos.close();
					s.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			}
		}
	}
	
	def deployVMs(DeployedCluster cluster){
		println "Deploying cluster ---> "+cluster.cluster.name
		for(image in cluster.images) {
			VirtualMachineStartMessage vmsm=new VirtualMachineStartMessage();
			println "Deploying Image ----->" +image.image.name+" "+image.virtualMachines.size()

			image.virtualMachines.eachWithIndex() { vm, i ->
				try{
					println "for(vm in image.virtualMachines){"
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

					println "String pmIp=vm.executionNode.ip.ip;"
					println vmsm
					try{
						println "Abriendo socket a "+pmIp+" "+variableManagerService.getIntValue("CLOUDER_CLIENT_PORT");
						Socket s=new Socket(pmIp,variableManagerService.getIntValue("CLOUDER_CLIENT_PORT"));
						ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
						oos.writeObject(vmsm);
						oos.flush();
						ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
						Object c=ois.readObject();
						oos.close();
						s.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	def stopVirtualMachine(VirtualMachineExecution vm){
		VirtualMachineStopMessage vmsm=new VirtualMachineStopMessage();
		println "vmsm.setExecutionTime"
		vmsm.setHypervisorName(1)
		println "vmsm.setHypervisorName"
		vmsm.setHypervisorPath("C:\\Program Files (x86)\\VMware\\VMware VIX\\vmrun.exe")
		println "vmsm.setHypervisorPath"
		vmsm.setVirtualMachineExecutionId(vm.id)
		println "vmsm.setVirtualMachineExecutionId"
		vmsm.setVmPath("D:\\DebianPaaS64\\DebianPaaS64.vmx")
		println "vmsm.setVmPath"
		String pmIp=vm.executionNode.ip.ip;
		println "String pmIp=vm.executionNode.ip.ip;"
		println vmsm
		try{
			Socket s=new Socket(pmIp,81);
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			oos.writeObject(vmsm);
			oos.flush();
			Object c=ois.readObject();
			oos.close();
			s.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
