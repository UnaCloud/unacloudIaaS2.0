package back.allocators

import unacloud2.*

class PhysicalMachineAllocatorService {
	
    def allocatePhysicalMachinesRandomly(DeployedCluster deployment){
		for(DeployedImage di:deployment.images){
			allocatePhysicalMachines(di);
		}
	}
	def allocatePhysicalMachine(VirtualMachineExecution vme ){
		
		List<PhysicalMachine> l=PhysicalMachine.list();
		Collections.shuffle(l);
		
		vme.executionNode = l.first();
		IPPool ipPool= vme.executionNode.laboratory.virtualMachinesIPs
		
		for(ip in ipPool.ips){
					if(ip.used==false){
						vme.ip= ip
						ip.used=true
						break
					}
				}
		
	}
	def allocatePhysicalMachines(DeployedImage deployedImage){
		List<PhysicalMachine> l=PhysicalMachine.list();
		Collections.shuffle(l);
		int a=0;
		for(VirtualMachineExecution vme:deployedImage.virtualMachines){
			if(a<l.size){
				vme.executionNode = l.get(a++);
				IPPool ipPool= vme.executionNode.laboratory.virtualMachinesIPs	
				
				for(ip in ipPool.ips){
					if(ip.used==false){
						vme.ip= ip
						ip.used=true
						break
					}
				}
				//vme.executionNode.laboratory.
			}
		}
	}
}