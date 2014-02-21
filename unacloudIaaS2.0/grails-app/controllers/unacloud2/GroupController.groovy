package unacloud2

class GroupController {
	
	GroupService groupService
	
	def beforeInterceptor = {
		if(!session.user){
			flash.message="You must log in first"
			redirect(uri:"/login", absolute:true)
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
		def group= new Grupo(name:(params.name))
		def users= params.users
		groupService.addGroup(group, users)
		redirect(controller:"group" ,action:"index")
	}
	def delete() {
		def group = Grupo.findByName(params.name)
		if (!group) {
		redirect(action:list)
		}
		
		else{
		groupService.deleteGroup(group)
		redirect(controller:"group" ,action:"index")
	
		}
	}
	
	def setPolicy(){
		Grupo g= Grupo.findByName(params.name)
		println "Grupo:"+g
		groupService.setPolicy(g, params.type,  params.value)
		redirect(action:"index")
	}
	
	def editPerms(){
		def g= Grupo.findByName(params.name)
		if (!g) {
			redirect(action:"index")
		}
		[group: g]		
	}
	
	def edit(){
		def g= Grupo.findByName(params.name)		
		if (!g)
		redirect(action:"index")
		else
		[users: User.list(params), group:g]
	}
	
	def setValues(){
		System.out.println(params.oldName)
		def group = Grupo.findByName(params.oldName)
		groupService.setValues(group, params.users,params.name)
		redirect(action:"index")
	}
}
