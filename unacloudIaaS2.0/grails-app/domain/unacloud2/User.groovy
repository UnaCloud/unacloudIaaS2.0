package unacloud2

import java.util.ArrayList;

class User {
	
	String name
	String username
	String userType
	String password
	Collection<UserPermss> userPermss
	
	static hasMany = [images: VirtualMachineImage, userClusters: Cluster]
	
	static constraints = {
    }
	
	ArrayList <PhysicalMachine> getOrderedImages(){
		VirtualMachineImageComparator c= new VirtualMachineImageComparator()
		ArrayList <VirtualMachineImage> array = new ArrayList(images).sort(c)
		return array
	}
}
