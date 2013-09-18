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
}
