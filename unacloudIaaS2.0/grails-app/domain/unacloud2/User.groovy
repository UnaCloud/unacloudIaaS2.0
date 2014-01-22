package unacloud2

import java.util.ArrayList;

class User {
	
	String name
	String username
	String userType
	String password
	String apiKey
	
	Collection<UserPermss> userPermss
	
	static hasMany = [images: VirtualMachineImage, userClusters: Cluster, deployments: Deployment]
	
	static constraints = {
    }
	
	ArrayList <VirtualMachineImage> getOrderedImages(){
		VirtualMachineImageComparator c= new VirtualMachineImageComparator()
		ArrayList <VirtualMachineImage> array = new ArrayList(images).sort(c)
		return array
	}
	
	ArrayList <Cluster> getOrderedClusters(){
		ClusterComparator c= new ClusterComparator()
		ArrayList <Cluster> array = new ArrayList(userClusters).sort(c)
		return array
	}
	
	def getActiveDeployments(){
		ArrayList activeDeployments= new ArrayList()
		for (deployment in deployments){
			if(!(deployment.status==(DeploymentStateEnum.FINISHED)))
				activeDeployments.add(deployment)
		}
		return activeDeployments
	}
}
