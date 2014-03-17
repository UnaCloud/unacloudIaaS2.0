package unacloud2

import java.util.ArrayList;

class User {
	
	String name
	String username
	String userType
	String password
	String apiKey
	
	static hasMany = [images: VirtualMachineImage, restrictions: UserRestriction, userClusters: Cluster, deployments: Deployment]
	
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
			if(deployment.isActive()) activeDeployments.add(deployment)
			else deployment.status='FINISHED'
		}
		return activeDeployments
	}
	
	
}
