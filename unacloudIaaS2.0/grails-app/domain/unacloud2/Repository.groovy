package unacloud2

class Repository {
	
	String name
	int capacity
	String root
	static hasMany = [images: VirtualMachineImage]
	
    static constraints = {
	}
	
}
