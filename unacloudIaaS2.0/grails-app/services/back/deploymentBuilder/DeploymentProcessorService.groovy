package back.deploymentBuilder;

import back.allocators.IpAllocatorService;
import back.allocators.PhysicalMachineAllocatorService;
import back.userRestrictions.UserRestrictionProcessorService;
import unacloud2.DeployedCluster;
import unacloud2.PhysicalMachine;
import unacloud2.PhysicalMachineStateEnum;
import unacloud2.User;
import unacloud2.VirtualMachineExecution;

class DeploymentProcessorService {
	UserRestrictionProcessorService userRestrictionProcessorService
	PhysicalMachineAllocatorService physicalMachineAllocatorService
	IpAllocatorService ipAllocatorService
	def doDeployment(DeployedCluster cluster,User user,boolean addInstancesDeployment){
		ArrayList<VirtualMachineExecution> vms=new ArrayList<>();
		List<PhysicalMachine> pms=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON);
		//userRestrictionProcessorService.applyUserPermissions(user,vms,pms)
		physicalMachineAllocatorService.allocatePhysicalMachines(cluster,vms,pms,addInstancesDeployment)
		ipAllocatorService.allocateIPAddresses(cluster, addInstancesDeployment)
	}
}
