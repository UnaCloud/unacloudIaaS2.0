package unacloud2

import java.util.Collection;

class PhysicalMachineRepository {
	
	String name
	int capacity
	String path
	static hasMany= {virtualMachines: VirtualMachine}
	static belongsTo = {phyMachine: PhysicalMachine}
	
    static constraints = {
    }
}
