package back.allocators

import unacloud2.DeployedCluster;
import unacloud2.DeployedImage;
import unacloud2.Deployment;
import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;

class PhysicalMachineAllocatorService {
	
    def allocatePhysicalMachinesRandomly(DeployedCluster deployment){
		for(DeployedImage di:deployment.images){
			allocatePhysicalMachines(di);
		}
	}
	def allocatePhysicalMachines(DeployedImage deployedImage){
		List<PhysicalMachine> l=PhysicalMachine.list();
		Collections.shuffle(l);
		int a=0;
		for(VirtualMachineExecution vme:deployedImage.virtualMachines){
			if(a<l.size){
				vme.executionNode = l.get(a++);
			}
		}
	}
}