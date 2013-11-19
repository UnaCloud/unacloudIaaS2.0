package unacloud2

class PhysicalMachine {

    String name
	boolean withUser
	int maxVirtualMachinesOn
	int cores
	int ram
	int hardDisk
	boolean highAvaliability
	String hypervisorPath
	IP ip
	String mac
	OperatingSystem operatingSystem
	MachineState state
	static hasMany = {VirtualMachine}
	static belongsTo ={laboratory: Laboratory}
	static constraints = {
    }
	
	
}
