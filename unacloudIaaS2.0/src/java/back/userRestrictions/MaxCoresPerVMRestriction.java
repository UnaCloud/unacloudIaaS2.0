package back.userRestrictions;

import java.util.List;

import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;

public class MaxCoresPerVMRestriction implements UserRestrictionInterface{

	@Override
	public void applyRestriction(String value,
			List<VirtualMachineExecution> vmes, List<PhysicalMachine> pms) throws UserRestrictionException {
		int cores= Integer.parseInt(value);
		System.out.println(cores);
		for (VirtualMachineExecution  vme:vmes){
			System.out.println("restriction:"+cores+" request:"+ vme.getCores());
			if (vme.getCores()>cores)
				throw new UserRestrictionException("Max cores allowed: "+cores);
		}
	}

}
