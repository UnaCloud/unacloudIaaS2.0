package unacloud2

import java.util.Collection;

class PhysicalMachineRepository {
	
	String name
	int capacity
	String path
	static hasMany= {images: VirtualMachine}
	static belongsTo = {phyMachine: PhysicalMachine}
	
    static constraints = {
    }
}
