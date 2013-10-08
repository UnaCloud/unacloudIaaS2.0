package unacloud2

class File {

    String fileName
	String route
	boolean templateFile
	
	static belongsTo = [image: VirtualMachineImage]
	static constraints = {
    }
}
