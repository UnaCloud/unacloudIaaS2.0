package unacloud2

import java.util.ArrayList;

class Cluster {
	
	String name
	static hasMany = [images: VirtualMachineImage]
	static constraints = {
    }	
	
	ArrayList <PhysicalMachine> getOrderedImages(){
		VirtualMachineImageComparator c= new VirtualMachineImageComparator()
		ArrayList <VirtualMachineImage> array = new ArrayList(images).sort(c)
		return array
	}
	
	def isDeployed(){
		boolean isDeployed=false
		def deployments= Deployment.findByStatusNotEqual(Deployment.FINISHED)
		deployments.each (){
			if(it.cluster.cluster==this)
				isDeployed=true
		}
		return isDeployed
	}
}
