package back.pmallocators;

import java.util.List;

import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;

public interface VirtualMachineAllocatorInterface {

	public void allocateVirtualMachines(List<VirtualMachineExecution> virtualMachineList,List<PhysicalMachine> physicalMachines);
}
