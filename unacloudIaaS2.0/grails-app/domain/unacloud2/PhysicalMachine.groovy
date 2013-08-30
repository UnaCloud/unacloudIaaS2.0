package unacloud2

class PhysicalMachine {

    String name
	int slots
	int cores
	int ram
	int hardDisk
	boolean highAvaliability
	String hypervisorPath
	IP ip
	String mac
	OperatingSystem operatingSystem
	
	static belongsTo ={laboratory: Laboratory}
	static constraints = {
    }
	
	
}
