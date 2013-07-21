package unacloud2

class User {
	
	String name
	String username
	String userType
	String password
	Collection<Template> userTemplates
	Collection<UserPermss> userPermss
	
	static hasMany = {images:VirtualMachineImage}
	
	
	static constraints = {
    }
	
	
}
