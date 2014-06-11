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
	def doDeployment(DeployedImage image,User user,boolean addInstancesDeployment) throws UserRestrictionException, AllocatorException{
		ArrayList<VirtualMachineExecution> vms=new ArrayList<>();
		if(!addInstancesDeployment)
		 vms.addAll(image.virtualMachines);
		else{
			println "adding only new instances for allocation"
			def virtualMachines= new ArrayList<>()
			for(VirtualMachineExecution vm:image.virtualMachines)
			if(vm.message.equals("Adding instance")){
				virtualMachines.add(vm)}
			vms.addAll(virtualMachines)
		}
		List<PhysicalMachine> pms
		if(image.highAvaliavility)
		pms=PhysicalMachine.findAllWhere(state:PhysicalMachineStateEnum.ON,highAvailability: true);
		else
		pms=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON);
		println "tamaño en doDeployment:"+pms.size()
		userRestrictionProcessorService.applyUserPermissions(user,vms,pms)
		println "tamaño después de userRestriction:"+pms.size()
		physicalMachineAllocatorService.allocatePhysicalMachines(image,vms,pms,addInstancesDeployment)
		ipAllocatorService.allocateIPAddresses(image, addInstancesDeployment)
	}
}
