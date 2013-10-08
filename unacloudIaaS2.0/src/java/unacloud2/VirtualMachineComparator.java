package unacloud2;

import java.util.Comparator;

public class VirtualMachineComparator implements Comparator<VirtualMachine>{
	
	@Override
	public int compare(VirtualMachine o1, VirtualMachine o2) {
		return (o1.getName()).compareTo(o2.getName());
	}

}
