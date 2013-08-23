package unacloud2

class VirtualMachineImage {
	
	String name
    //boolean avaliable
	boolean isPublic
	boolean customizable
	int fixedDiskSize
	String user
	String password
	String volume
	OperatingSystem operatingSystem
	
	static hasMany = [files:File ]
	
	static constraints = {
    }
}
