package unacloud2

class File {

    String fileName
	String route
	
	static belongsTo = [image: VirtualMachineImage]
	static constraints = {
    }
}
