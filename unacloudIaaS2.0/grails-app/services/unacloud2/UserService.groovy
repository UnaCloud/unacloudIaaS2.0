package unacloud2

class UserService {

    def addUser(String username, String name, String userType, String password) {
	   def u= new User(username: username, name: name, userType: userType, password:password )
	   u.save()
	   
    }
	
	def deleteUser(User user){
		user.delete()
	}
	
	def setValues(User user, String username, String name, String userType, String password){
		user.putAt("username", username)
		user.putAt("password", password)
		user.putAt("name", name)
		user.putAt("userType", userType)		
	}
}
