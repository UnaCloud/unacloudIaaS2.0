package back.pmallocators

import back.services.PhysicalMachineStateManagerService;
import java.util.Comparator;
import unacloud2.*

class PhysicalMachineAllocatorService {
	def allocatePhysicalMachines(DeployedCluster cluster){
		ArrayList<VirtualMachineExecution> vms=new ArrayList<>();
		List<PhysicalMachine> pms=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON);
		for(DeployedImage image:cluster.images){
			vms.addAll(image.virtualMachines);
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
	
}