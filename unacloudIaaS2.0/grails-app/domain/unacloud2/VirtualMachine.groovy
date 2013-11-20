package unacloud2

class VirtualMachine {
	
	VirtualMachineImage image
	String folderName
	long size
	VirtualMachineStateEnum state
	static belongsTo = [ repo: PhysicalMachineRepository]
	
    static constraints = {
    }
}
