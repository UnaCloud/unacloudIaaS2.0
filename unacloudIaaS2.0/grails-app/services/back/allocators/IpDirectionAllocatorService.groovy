package back.allocators

import unacloud2.DeployedCluster;
import unacloud2.DeployedImage;
import unacloud2.Deployment;
import unacloud2.Laboratory;
import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;

class IpDirectionAllocatorService {

    def allocateIPsRandomly(DeployedCluster deployment){
		for(DeployedImage di:deployment.images){
			for(VirtualMachineExecution vme:virtualMachines){
				PhysicalMachine pm=vme.executionNode;
				Laboratory lab=pm.laboratory;
				lab.get
			}
		}
		
		//deployment.
	}
	
}
