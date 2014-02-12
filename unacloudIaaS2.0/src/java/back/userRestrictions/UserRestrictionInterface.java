package back.userRestrictions;

import java.util.List;

import unacloud2.DeployedCluster;
import unacloud2.PhysicalMachine;
import unacloud2.VirtualMachineExecution;
import unacloud2.User;

public interface UserRestrictionInterface {

	public void applyRestriction(String value,List<VirtualMachineExecution> vmes,List<PhysicalMachine> pms) throws UserRestrictionException;
}
