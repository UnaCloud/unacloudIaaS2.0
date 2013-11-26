package back.allocators

import unacloud2.*

class PhysicalMachineAllocatorService {
	
    def allocatePhysicalMachinesRandomly(DeployedCluster deployment){
		println "allocando"
		for(DeployedImage di:deployment.images){
			allocatePhysicalMachines(di);
		}
		println "allocado completo"
	}
	
	def allocatePhysicalMachine(VirtualMachineExecution vme ){
		
		List<PhysicalMachine> l=PhysicalMachine.list();
		Collections.shuffle(l);
		
		vme.executionNode = l.first();
		IPPool ipPool= vme.executionNode.laboratory.virtualMachinesIPs
		
		for(ip in ipPool.ips){
					if(ip.used==false){
						vme.ip= ip
						ip.mask=ipPool.mask
						ip.used=true
						break
					}
				}
		
	}
	def allocatePhysicalMachines(DeployedImage deployedImage){
		List<PhysicalMachine> l=PhysicalMachine.list();
		Collections.shuffle(l);
		int a=0;
		println "allocando imagenes"
		for(VirtualMachineExecution vme:deployedImage.virtualMachines){
			if(a<l.size){
				println "allocando vm"
				vme.executionNode = l.get(a++);
				IPPool ipPool= vme.executionNode.laboratory.virtualMachinesIPs	
				
				for(ip in ipPool.ips){
					println "buscando ip"
					if(ip.used==false){
						println "ip encontrada"
						vme.putAt("ip", ip)
						ip.putAt("used", true)
						break
					}
				}
				//vme.executionNode.laboratory.
			}
		}
		println "allocado"
	}
}