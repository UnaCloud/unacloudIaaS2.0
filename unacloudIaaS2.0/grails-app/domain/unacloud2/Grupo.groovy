package unacloud2

class Grupo {
	
	String name
		
	static hasMany = [users: User]
	static constraints = {
    
	}
}
