package unacloud2

class GroupController {

	def beforeInterceptor = {
		if(!session.user){
			flash.message="You must log in first"
			redirect(uri:"/", absolute:true)
			return false
		}
		else if(!(session.user.userType.equals("Administrator"))){
			flash.message="You must be administrator to see this content"
			redirect(uri:"/error", absolute:true)
			return false
		}
	}
	
	def index() {
		[groups: Grupo.list(params)]
	}
	
	def create(){
		[users: User.list(params)]
	}
	
	def add(){
		def g= new Grupo(name:(params.name))
		g.users = []
		if(params.users.getClass().equals(String))
		g.users.add(User.findByUsername(params.users))
		else{
		for(username in params.users){
			g.users.add(User.findByUsername(username))
		}
		}
		g.save()
		redirect(controller:"group" ,action:"index")
	}
	def delete() {
		redirect(controller:"user", action: "validateSession")
		def group = Grupo.findByName(params.name)
		if (!group) {
		redirect(action:list)
		}
		
		else{
		group.delete()
		redirect(controller:"group" ,action:"index")
	
		}
	}
	
	def edit(){
		def g= Grupo.findByName(params.name)		
		if (!g)
		redirect(action:"index")
		else
		[users: User.list(params), group:g]
	}
	
	def setValues(){
		UserController.validateSession()
		System.out.println(params.oldName)
		def group = Grupo.findByName(params.oldName)
		group.putAt("name", params.name)
		Set newUsers= []
		if(params.users.getClass().equals(String))
		newUsers.add(User.findByUsername(params.users))
		
		else{
		for(username in params.users){
			newUsers.add(User.findByUsername(username))
		}
		}
		group.putAt("users", newUsers)
		redirect(action:"index")
	}
}
