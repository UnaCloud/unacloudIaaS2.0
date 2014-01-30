package back.deploymentBuilder;

import back.ipallocators.IpAllocatorService;
import back.pmallocators.PhysicalMachineAllocatorService
import back.userRestrictions.UserRestrictionProcessorService;
import unacloud2.DeployedCluster;

class DeploymentProcessorService {
	UserRestrictionProcessorService userRestrictionProcessorService
	PhysicalMachineAllocatorService physicalMachineAllocatorService
	IpAllocatorService ipAllocatorService
	def doDeployment(DeployedCluster cluster,boolean addInstancesDeployment){
		userRestrictionProcessorService.applyUserPermissions(cluster)
		physicalMachineAllocatorService.allocatePhysicalMachines(cluster,addInstancesDeployment)
		ipAllocatorService.allocateIPAddresses(cluster, addInstancesDeployment)
	}
}
