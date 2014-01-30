package back.pmallocators;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;

public class RoundRobinAllocator implements VirtualMachineAllocatorInterface {
	private static final int MAX_VM_PER_PM = 1;

	@Override
	public void allocateVirtualMachines(
			List<VirtualMachineExecution> virtualMachineList,
			List<PhysicalMachine> physicalMachines,
			Map<Long, PhysicalMachineAllocationDescription> physicalMachineDescriptions) {
		Collections.sort(physicalMachines, new Comparator<PhysicalMachine>() {
			public int compare(PhysicalMachine p1, PhysicalMachine p2) {
				return Long.compare(p1.getDatabaseId(), p2.getDatabaseId());
			}
		});
		for (int nextVm = 0, lastNextVm = 0; nextVm < virtualMachineList.size();) {
			List<PhysicalMachine> current=physicalMachines;
			List<PhysicalMachine> next=new LinkedList<>();
			for (PhysicalMachine pm : physicalMachines) {
				if (nextVm >= virtualMachineList.size())
					break;
				PhysicalMachineAllocationDescription pmad = physicalMachineDescriptions
						.get(pm.getDatabaseId());
				VirtualMachineExecution nextVirtualMachine = virtualMachineList
						.get(nextVm);
				if (pmad == null
						&& nextVirtualMachine.getCores() <= pm.getCores()
						&& nextVirtualMachine.getRam() <= pm.getRam()) {
					nextVirtualMachine.setExecutionNode(pm);
					pmad = new PhysicalMachineAllocationDescription(
							pm.getDatabaseId(), nextVirtualMachine.getCores(),
							nextVirtualMachine.getRam(), 1);
					physicalMachineDescriptions.put(pm.getDatabaseId(), pmad);
					System.out.println("Asigne " + pm.getDatabaseId()
							+ " no tenia vms");
					nextVm++;
					next.add(pm);
				} else if (pmad.getVms() < MAX_VM_PER_PM
						&& pmad.getCores() + nextVirtualMachine.getCores() <= pm
								.getCores()
						&& pmad.getRam() + nextVirtualMachine.getRam() <= pm
								.getRam()) {
					nextVirtualMachine.setExecutionNode(pm);
					pmad.addResources(nextVirtualMachine.getCores(),
							nextVirtualMachine.getRam(), 1);
					nextVm++;
					System.out.println("Asigne " + pm.getDatabaseId()
							+ " tenia vms");
					next.add(pm);
				} else {
					System.out.println("No use " + pm.getDatabaseId());
				}
			}
			current=next;next=new LinkedList<>();
			if (lastNextVm == nextVm) {
				// Hubo un error no se pudo asignar todas las VMs
				break;
			}
			lastNextVm = nextVm;
		}
	}

	

}
