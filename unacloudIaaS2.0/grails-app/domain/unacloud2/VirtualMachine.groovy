package unacloud2

class VirtualMachine {
	
	VirtualMachineImage image
	String folderName
	long size
	VirtualMachineStateEnum state = VirtualMachineStateEnum.NO_COPY
	static belongsTo = [ repo: PhysicalMachineRepository]
	
    static constraints = {
    }
}
