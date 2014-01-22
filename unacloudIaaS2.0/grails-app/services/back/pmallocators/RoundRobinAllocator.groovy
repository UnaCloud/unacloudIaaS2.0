package back.pmallocators;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;

public class RoundRobinAllocator implements VirtualMachineAllocatorInterface {

	@Override
	public void allocateVirtualMachines(List<VirtualMachineExecution> virtualMachineList,List<PhysicalMachine> physicalMachines) {
		Collections.sort(physicalMachines,new Comparator<PhysicalMachine>(){
			public int compare(PhysicalMachine p1,PhysicalMachine p2){
				return Long.compare(p1.getDatabaseId(),p2.getDatabaseId());
			}
		});
		for(int e=0;e<virtualMachineList.size();e++){
			virtualMachineList.get(e).setExecutionNode(physicalMachines.get(e/2));
		}
	}

}
