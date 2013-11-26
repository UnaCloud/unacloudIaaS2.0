package back.deployers

import javassist.bytecode.stackmap.BasicBlock.Catch;

import com.losandes.utils.Constants;

import communication.messages.vmo.VirtualMachineStartMessage;
import unacloud2.*;

class DeployerService {
	
	TransferService transferService
	
    def	deploy(Deployment deployment){
		
		println "llamando"
		//transferService.transferVM(deployment.cluster)
		println "llamé"
		deployVMs(deployment.cluster);	
		
	}
	
		def deployVMs(DeployedCluster cluster){
			println "Deploying cluster ---> "+cluster.cluster.name
			for(image in cluster.images) {
				println "Deploying Image ----->" +image.image.name
				VirtualMachineStartMessage vmsm=new VirtualMachineStartMessage();
				vmsm.setPassword(image.image.password)
				vmsm.setUsername(image.image.user)
				vmsm.setConfiguratorClass(image.image.operatingSystem.configurer)
				println "Deploying Image ----->" +image.image.name
				
				for(vm in image.virtualMachines){
					println "Deploying VM"+ vm.name
					
					vmsm.setExecutionTime(1)
					println "tiempo asignado"
					vmsm.setHypervisorName(1)
					vmsm.setHypervisorPath("C:\\Program Files (x86)\\VMware\\VMware VIX\\vmrun.exe")
					vmsm.setHostname("debian6")
					println "Deploying2 VM"+ vm.name
					vmsm.setVirtualMachineIP(vm.ip.ip)
					vmsm.setVirtualMachineNetMask("255.255.255.0")
					vmsm.setVmCores(vm.cores)
					vmsm.setVmMemory(vm.ram)
					println "Deploying3 VM"+ vm.name
					vmsm.setVmPath("D:\\DebianPaaS64\\DebianPaaS64.vmx")
					String pmIp=vm.executionNode.ip.ip;
					println vmsm
					
					try{
						println "conectando a ip "+pmIp
						Socket s=new Socket(pmIp,81);
						ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
						ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
						oos.writeObject(vmsm);
						oos.flush();
						Object c=ois.readObject();
						println "response: "+c	
						oos.close();
						
						s.close();
					}catch(Exception e){
						e.printStackTrace();
					}
					if(vm.status == VirtualMachineExecutionStateEnum.DEPLOYING){
						vm.status = VirtualMachineExecutionStateEnum.DEPLOYED
						vm.save()
					}
				}
			}
	}

}
