package back.pmallocators;

import java.util.List;
import java.util.Map;

import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;

public abstract class VirtualMachineAllocator{

	public abstract void allocateVirtualMachines(List<VirtualMachineExecution> virtualMachineList,List<PhysicalMachine> physicalMachines,Map<Long,PhysicalMachineAllocationDescription> physicalMachineDescriptions)throws AllocatorException;
	public boolean fitVMonPM(VirtualMachineExecution vme,PhysicalMachine pm,PhysicalMachineAllocationDescription pmad){
		if (pmad == null && vme.getHardwareProfile().getCores() <= pm.getCores() && vme.getHardwareProfile().getRam() <= pm.getRam()) {
			return true;
		} else if (pmad!= null && pmad.getCores() + vme.getHardwareProfile().getCores() <= pm.getCores()&& pmad.getRam() + vme.getHardwareProfile().getRam() <= pm.getRam()) {
			return true;
		}
		return false;
	}
}
