package unacloud2

import java.util.Collection;

class PhysicalMachineRepository {
	
	String name
	int capacity
	String path
	static hasMany= [virtualMachines: VirtualMachine]
	static belongsTo = [phyMachine: PhysicalMachine]
	
    static constraints = {
    }
	def VirtualMachine getFreeVirtualMachine(VirtualMachineImage image){
		for(VirtualMachine vm:virtualMachines){
			if(vm.image.id == image.id && vm.state == VirtualMachineStateEnum.FREE){
				return vm;
			}
		}
		return null;
	}
}
