package back.deploymentBuilder;

import org.springframework.aop.ThrowsAdvice;

import back.allocators.IpAllocatorService;
import back.allocators.PhysicalMachineAllocatorService;
import back.pmallocators.AllocatorException;
import back.userRestrictions.UserRestrictionException;
import back.userRestrictions.UserRestrictionProcessorService;
import unacloud2.DeployedCluster;
import unacloud2.DeployedImage
import unacloud2.PhysicalMachine;
import unacloud2.PhysicalMachineStateEnum;
import unacloud2.User;
import unacloud2.VirtualMachineExecution;

class DeploymentProcessorService {
	UserRestrictionProcessorService userRestrictionProcessorService
	PhysicalMachineAllocatorService physicalMachineAllocatorService
	IpAllocatorService ipAllocatorService
	def doDeployment(DeployedCluster cluster,User user,boolean addInstancesDeployment) throws UserRestrictionException, AllocatorException{
		ArrayList<VirtualMachineExecution> vms=new ArrayList<>();
		if(!addInstancesDeployment)
		for(DeployedImage image:cluster.images)vms.addAll(image.virtualMachines);
		else{
		println "adding only new instances for allocation"
		for(DeployedImage image:cluster.images){
			
			def virtualMachines= new ArrayList<>()
			for(VirtualMachineExecution vm:image.virtualMachines)
			if(vm.message.equals("Adding instance")){
				virtualMachines.add(vm)}
			vms.addAll(virtualMachines)
			}
		}
		List<PhysicalMachine> pms=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON);
		println "tamaño en doDeployment:"+pms.size()
		userRestrictionProcessorService.applyUserPermissions(user,vms,pms)
		println "tamaño después de userRestriction:"+pms.size()
		physicalMachineAllocatorService.allocatePhysicalMachines(cluster,vms,pms,addInstancesDeployment)
		ipAllocatorService.allocateIPAddresses(cluster, addInstancesDeployment)
	}
}
