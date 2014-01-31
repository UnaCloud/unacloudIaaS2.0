package back.pmallocators;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;

public class RandomAllocator extends VirtualMachineAllocator {

	@Override
	public void allocateVirtualMachines(List<VirtualMachineExecution> virtualMachineList,List<PhysicalMachine> physicalMachines,Map<Long,PhysicalMachineAllocationDescription> physicalMachineDescriptions) {
		if(virtualMachineList.size()>2*physicalMachines.size()){
		}else{
			Collections.shuffle(virtualMachineList);
			Collections.shuffle(physicalMachines);
			for(int e=0;e<virtualMachineList.size();e++){
				virtualMachineList.get(e).setExecutionNode(physicalMachines.get((int)(e/2)));
			}
		}
		
	}

}
