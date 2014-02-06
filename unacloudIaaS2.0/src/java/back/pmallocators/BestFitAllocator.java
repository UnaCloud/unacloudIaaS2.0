package back.pmallocators;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import back.pmallocators.FirstFitDecreasingAllocator.PhysicalMachineComparator;
import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;

public class BestFitAllocator extends VirtualMachineAllocator {
	
	public class PhysicalMachineComparator implements Comparator<PhysicalMachine>{
		Map<Long, PhysicalMachineAllocationDescription> physicalMachineDescriptions;
		
		public PhysicalMachineComparator(Map<Long, PhysicalMachineAllocationDescription> physicalMachineDescriptions) {
			this.physicalMachineDescriptions=physicalMachineDescriptions;
		}
		
		public int compare(PhysicalMachine p1, PhysicalMachine p2) {
			PhysicalMachineAllocationDescription pmad1=physicalMachineDescriptions.get(p1.getDatabaseId());
			PhysicalMachineAllocationDescription pmad2=physicalMachineDescriptions.get(p2.getDatabaseId());
			int coresUsados1=pmad1==null?0:pmad1.cores,coresUsados2=pmad2==null?0:pmad2.cores;
			int cores=Integer.compare(p1.getCores()-coresUsados1,p2.getCores()-coresUsados2);
			return cores;
		}
	}
	
	@Override
	public void allocateVirtualMachines(List<VirtualMachineExecution> virtualMachineList,List<PhysicalMachine> physicalMachines,final Map<Long, PhysicalMachineAllocationDescription> physicalMachineDescriptions)throws AllocatorException{
		Collections.sort(physicalMachines, new PhysicalMachineComparator(physicalMachineDescriptions));
		Collections.sort(virtualMachineList, new Comparator<VirtualMachineExecution>() {
			public int compare(VirtualMachineExecution v1, VirtualMachineExecution v2) {
				return Integer.compare(v2.getCores(),v1.getCores());
			}
		});
		for(PhysicalMachine pm:physicalMachines){
			System.out.println(pm.getDatabaseId()+"\t"+pm.getCores()+"\t"+pm.getRam()+"\t"+physicalMachineDescriptions.get(pm.getDatabaseId()));
		}
		vmCycle:for(VirtualMachineExecution vme:virtualMachineList){
			for(PhysicalMachine pm:physicalMachines){
				PhysicalMachineAllocationDescription pmad = physicalMachineDescriptions.get(pm.getDatabaseId());
				if(fitVMonPM(vme, pm, pmad)){
					vme.setExecutionNode(pm);
					if(pmad==null){
						pmad=new PhysicalMachineAllocationDescription(pm.getDatabaseId(),0,0,0);
						physicalMachineDescriptions.put(pmad.getNodeId(),pmad);
					}
					pmad.addResources(vme.getCores(),vme.getRam(), 1);
					Collections.sort(physicalMachines, new PhysicalMachineComparator(physicalMachineDescriptions));
					continue vmCycle;
				}
			}
			throw new AllocatorException("Cannot allocate all VMs on available insfrastructure");
		}
	}
}
