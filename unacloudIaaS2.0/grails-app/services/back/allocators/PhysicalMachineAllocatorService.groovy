package back.allocators

import unacloud2.DeployedCluster;
import unacloud2.DeployedImage;
import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;

class PhysicalMachineAllocatorService {

    def allocatePhysicalMachinesRandomly(DeployedCluster deployment){
		
	}
	def allocatePhysicalMachines(DeployedImage deployedImage){
		List<PhysicalMachine> l=PhysicalMachine.findAll();
		Collections.shuffle(l);
		int a=0;
		for(VirtualMachineExecution vme:deployedImage.virtualMachines){
			while(a<l.size){
				//vme.
			}
		}
	}
}
