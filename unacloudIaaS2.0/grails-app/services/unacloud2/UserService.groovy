package unacloud2

import org.apache.commons.lang.RandomStringUtils;

class UserService {

    def addUser(String username, String name, String userType, String password) {
	   String charset = (('A'..'Z') + ('0'..'9')).join()
	   Integer length = 32
	   String randomString = RandomStringUtils.random(length, charset.toCharArray())
	   def u= new User(username: username, name: name, userType: userType, password:password , apiKey: randomString )
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
	
	def setPolicy(User u, String name, String value){
		UserRestriction oldAlloc
		for(allocPolicy in u.restrictions){
			if(allocPolicy.name.equals(name)){
				oldAlloc= allocPolicy
			}
		}
		println "alloc found:"+oldAlloc
		if(oldAlloc==null){
			
			def alloc= new UserRestriction(name: name, value: value)
			alloc.save(failOnError: true)
			println "alloc created:"+alloc
			u.addToRestrictions(alloc)
			u.save(failOnError: true)
		}
		else{
			println "setting value on oldAlloc:"+oldAlloc
			oldAlloc.setValue(value)
			oldAlloc.save(failOnError: true)
		}
	}
	
	def changePass(User u, String newPass){
		u.password=newPass
		u.save()	
	}
	
	def refreshAPIKey(User u){
		String charset = (('A'..'Z') + ('0'..'9')).join()
		Integer length = 32
		String randomString = RandomStringUtils.random(length, charset.toCharArray())
		u.apiKey=randomString
		u.save()
		return u.apiKey
	}
}
