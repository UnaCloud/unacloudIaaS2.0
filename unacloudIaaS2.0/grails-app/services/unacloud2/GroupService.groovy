package unacloud2

class GroupService {

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
