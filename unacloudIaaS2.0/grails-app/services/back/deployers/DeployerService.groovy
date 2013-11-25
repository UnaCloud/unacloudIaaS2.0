package back.deployers

import javassist.bytecode.stackmap.BasicBlock.Catch;

import com.losandes.utils.Constants;

import communication.messages.vmo.VirtualMachineStartMessage;
import unacloud2.*;

class DeployerService {
	
	TransferService transferService
	
    def	deploy(Deployment deployment){
		runAsync{
			transferService.transferVM(deployment.cluster)
			sleep(10000)
			deployVMs(deployment.cluster);	
		}
	}
	
		def deployVMs(DeployedCluster cluster){
			for(image in cluster.images) {
				VirtualMachineStartMessage vmsm=new VirtualMachineStartMessage();
				vmsm.setPassword(image.image.password)
				vmsm.setUsername(image.image.user)
				vmsm.setConfiguratorClass(image.image.operatingSystem.configurer)
				vmsm.setHypervisorName()
				vmsm.setHypervisorPath()
				
				for(vm in image.virtualMachines){
					vmsm.setExecutionTime(vm.stopTime()-vm.startTime())
					vmsm.setHypervisorName(Constants.VMW)
					vmsm.setHypervisorPath("C:\\Program Files (x86)\\VMware\\VMware VIX\\vmrun.exe")
					vmsm.setHostname("debian6")
					vmsm.setVirtualMachineIP(vm.ip.ip)
					vmsm.setVirtualMachineNetMask(vm.ip.ipPool.mask)
					vmsm.setVmCores(vm.cores)
					vmsm.setVmMemory(vm.ram)
					vmsm.setVmPath("D:\\Debian 6 - Lab Heroku Est\\Debian 6 Lab Heroku.vmx")
					try{
						Socket s=new Socket(vm.executionNode.ip.ip);
						ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
						ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
						oos.writeObject(vmsm);
						oos.close();
						Object c=ois.readObject();
						println "response: "+c	
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
