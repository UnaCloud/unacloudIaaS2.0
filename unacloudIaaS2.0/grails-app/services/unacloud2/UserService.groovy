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
		AllocationPolicy oldAlloc
		for(allocPolicy in u.allocationPolicies){
			if(allocPolicy.name.equals(name)){
				oldAlloc= allocPolicy
			}
		}
		println oldAlloc
		if(oldAlloc==null){
			def alloc= new AllocationPolicy(name: name, value: value)
			alloc.save()
			println alloc
			u.addToAllocationPolicies(alloc).save()
		}
		else{
			println oldAlloc
			oldAlloc.setValue(value).save()
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
