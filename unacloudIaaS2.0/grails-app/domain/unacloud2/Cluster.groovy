package unacloud2

import java.util.ArrayList;

class Cluster {
	
	String name
	static hasMany = [images: VirtualMachineImage]
	
	
	List <PhysicalMachine> getOrderedImages(){
		return images.sort()
		
	}
	
	def isDeployed(){
		boolean isDeployed=false
		def deployments= Deployment.findByStatusNotEqual(DeploymentStateEnum.FINISHED)
		deployments.each (){
			if(it.cluster.cluster==this)
				isDeployed=true
		}
		return isDeployed
	}	
	
}
