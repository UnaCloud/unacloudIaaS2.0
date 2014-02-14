package back.pmallocators;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;

public class RoundRobinAllocator extends VirtualMachineAllocator {
	private static final int MAX_VM_PER_PM = 1;

	@Override
	public void allocateVirtualMachines(List<VirtualMachineExecution> virtualMachineList,List<PhysicalMachine> physicalMachines,Map<Long, PhysicalMachineAllocationDescription> physicalMachineDescriptions)throws AllocatorException{
		Collections.sort(physicalMachines, new Comparator<PhysicalMachine>() {
			public int compare(PhysicalMachine p1, PhysicalMachine p2) {
				return Long.compare(p1.getDatabaseId(), p2.getDatabaseId());
			}
		});
		for (int nextVm = 0, lastNextVm = 0; nextVm < virtualMachineList.size();) {
			List<PhysicalMachine> current=physicalMachines;
			List<PhysicalMachine> next=new LinkedList<>();
			for (PhysicalMachine pm : current) {
				System.out.println("evaluating machine: "+pm.getName());
				if (nextVm >= virtualMachineList.size())break;
				PhysicalMachineAllocationDescription pmad = physicalMachineDescriptions.get(pm.getDatabaseId());
				VirtualMachineExecution nextVirtualMachine = virtualMachineList.get(nextVm);
				if(fitVMonPM(nextVirtualMachine, pm, pmad)&&(pmad==null||pmad.getVms()<MAX_VM_PER_PM)){
					nextVirtualMachine.setExecutionNode(pm);
					next.add(pm);
					if(pmad==null){
						pmad=new PhysicalMachineAllocationDescription(pm.getDatabaseId(),0,0,0);
						physicalMachineDescriptions.put(pmad.getNodeId(),pmad);
					}
					pmad.addResources(nextVirtualMachine.getCores(),nextVirtualMachine.getRam(), 1);
					nextVm++;
				}
			}
			current=next;next=new LinkedList<>();
			if (lastNextVm == nextVm) {
				throw new AllocatorException("Cannot allocate all VMs on available insfrastructure");
			}
			lastNextVm = nextVm;
		}
	}
}
