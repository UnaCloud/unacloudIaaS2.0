package unacloud2

class VirtualMachine {
	
	VirtualMachineImage image
	String folderName
	int size
	VirtualMachineStateEnum state
	static belongsTo = { repo: PhysicalMachineRepository}
	
    static constraints = {
    }
}
