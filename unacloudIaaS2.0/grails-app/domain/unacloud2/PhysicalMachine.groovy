package unacloud2

class PhysicalMachine {

    String name
	boolean withUser
	int cores
	int ram
	boolean highAvaliability
	String hypervisorPath
	IP ip
	String mac
	PhysicalMachineStateEnum state
	OperatingSystem operatingSystem
	
	static hasMany = {repo: PhysicalMachineRepository}
	
	static belongsTo ={laboratory: Laboratory}
	static constraints = {
    }
	
	
}
