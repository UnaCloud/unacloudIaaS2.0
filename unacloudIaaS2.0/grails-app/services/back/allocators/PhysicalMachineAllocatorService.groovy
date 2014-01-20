package back.allocators

import back.services.PhysicalMachineStateManagerService;
import java.util.Comparator;
import unacloud2.*

class PhysicalMachineAllocatorService {
//<<<<<<< HEAD
	def allocatePhysicalMachines(DeployedCluster cluster){
		ArrayList<VirtualMachineExecution> vms=new ArrayList<>();
		List<PhysicalMachine> pms=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON);
		for(DeployedImage image:cluster.images){
			vms.addAll(image.virtualMachines);
/*=======

	def allocatePhysicalMachinesRandomly(DeployedCluster deployment){
		println "allocando"
		for(DeployedImage di:deployment.images){
			allocatePhysicalMachines(di);
>>>>>>> branch 'master' of https://github.com/UnaCloud/unacloudIaaS2.0.git*/
		}
		if(cluster.allocPolicies==null||cluster.allocPolicies.isEmpty()){
			VirtualMachineAllocatorInterface allocator=new RoundRobinAllocator();
			allocator.allocateVirtualMachines(vms,pms);
			for(VirtualMachineExecution vme:vms){
				println "Allocando en : "+vme.executionNode.ip.ip;
				IPPool ipPool= vme.executionNode.laboratory.virtualMachinesIPs
				for(ip in ipPool.ips){
					if(ip.used==false){
						vme.ip= ip
						ip.used=true
						break
					}
				}
			}
		}
	}
//<<<<<<< HEAD
/*=======

>>>>>>> branch 'master' of https://github.com/UnaCloud/unacloudIaaS2.0.git*/
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
/*<<<<<<< HEAD
		
=======
>>>>>>> branch 'master' of https://github.com/UnaCloud/unacloudIaaS2.0.git*/
	}
	/*def allocatePhysicalMachines(DeployedImage deployedImage){
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
	}*/
}
