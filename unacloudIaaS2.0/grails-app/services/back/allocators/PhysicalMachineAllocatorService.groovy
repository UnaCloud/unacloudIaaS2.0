package back.allocators

import back.services.PhysicalMachineStateManagerService;
import java.util.Comparator;
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

		List<PhysicalMachine> l=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON);
		Collections.sort(l,new Comparator<PhysicalMachine>(){
					public int compare(PhysicalMachine p1,PhysicalMachine p2){
						return Long.compare(p1.id,p2.id);
					}
				});

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
		List<PhysicalMachine> l=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON)
		Collections.sort(l,new Comparator<PhysicalMachine>(){
					public int compare(PhysicalMachine p1,PhysicalMachine p2){
						return Long.compare(p1.id,p2.id);
					}
				});
		int a=0;
		println "allocando imagenes"
		for(vme in deployedImage.virtualMachines){
			if(a<l.size){
				println "allocando vm"
				vme.executionNode = l.get(a++);
				//TODO, si esto se quita falla por lazy loading.
				println "PM IP is "+vme.executionNode.ip.ip;
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
			}
		}
		println "allocado"
	}
}