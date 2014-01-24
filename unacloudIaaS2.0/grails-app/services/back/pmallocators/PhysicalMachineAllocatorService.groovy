package back.pmallocators

import back.services.PhysicalMachineStateManagerService;
import java.util.Comparator;
import unacloud2.*

class PhysicalMachineAllocatorService {
	def allocatePhysicalMachines(DeployedCluster cluster){
		ArrayList<VirtualMachineExecution> vms=new ArrayList<>();
		List<PhysicalMachine> pms=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON);
		for(DeployedImage image:cluster.images)vms.addAll(image.virtualMachines);
		ServerVariable allocatorName=ServerVariable.findByName("VM_ALLOCATOR_NAME");
		if(allocatorName==null){
			AllocatorEnum.RANDOM.getAllocator().allocateVirtualMachines(vms,pms);
		}else{
			AllocatorEnum allocEnum=AllocatorEnum.valueOf(allocatorName);
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
