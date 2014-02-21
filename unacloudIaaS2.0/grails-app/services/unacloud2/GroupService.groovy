package unacloud2

class GroupService {
	
	UserService userService
	
    def addGroup(Grupo g, users){
		g.users = []
		if(users.getClass().equals(String))
		g.users.add(User.findByUsername(users))
		else{
		for(username in users){
			g.users.add(User.findByUsername(username))
		}
		}
		g.save()
    }
	
	def deleteGroup(Grupo g){
		g.delete()
	}
	
	def setPolicy(Grupo g, String name, String value){
		for(user in g.users){
			userService.setPolicy(user, name, value)
		}
	}
	
	def setValues(Grupo group, users,String name){
		group.putAt("name", name)
		Set newUsers= []
		if(users.getClass().equals(String))
		newUsers.add(User.findByUsername(users))
		
		else{
		for(username in users){
			newUsers.add(User.findByUsername(username))
		}
		}
		group.putAt("users", newUsers)
		
	}
}
