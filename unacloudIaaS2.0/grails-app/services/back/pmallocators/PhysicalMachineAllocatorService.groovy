package back.pmallocators

import back.services.PhysicalMachineStateManagerService;

import java.util.Comparator;

import javassist.bytecode.stackmap.BasicBlock.Catch;
import unacloud2.*

class PhysicalMachineAllocatorService {
	def allocatePhysicalMachines(DeployedCluster cluster){
		ArrayList<VirtualMachineExecution> vms=new ArrayList<>();
		List<PhysicalMachine> pms=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON);
		for(DeployedImage image:cluster.images)vms.addAll(image.virtualMachines);
		ServerVariable allocatorName=ServerVariable.findByName("VM_ALLOCATOR_NAME");
		AllocatorEnum allocator=AllocatorEnum.ROUND_ROBIN;
		if(allocatorName!=null){
			AllocatorEnum allocEnum=AllocatorEnum.valueOf(allocatorName);
			if(allocEnum!=null)allocator=allocEnum;
		}
		allocator.getAllocator().allocateVirtualMachines(vms,pms);
	}
	def allocatePhysicalMachine(VirtualMachineExecution vme ){
		List<PhysicalMachine> l=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON);
		Collections.sort(l,new Comparator<PhysicalMachine>(){
			public int compare(PhysicalMachine p1,PhysicalMachine p2){
				return Long.compare(p1.id,p2.id);
			}
		});

		vme.executionNode = l.first();
	}
	
}
