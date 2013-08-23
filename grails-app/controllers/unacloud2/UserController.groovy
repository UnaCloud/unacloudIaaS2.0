package unacloud2

class UserController {
	
	def beforeInterceptor = [action:{
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
	}, except: ['login','logout','home','userHome']]
	
    def index() {
		[users: User.list(params)];
	}
	
	def home(){
		if(!session.user){
			flash.message="You must log in first"
			redirect(uri:"/", absolute:true)
			return false
		}
		else if(!(session.user.userType.equals("Administrator")))
			redirect(uri:"/userHome", absolute:true)
		else{
			redirect(uri:"/adminHome", absolute:true)
		}
	}
	
	def adminHome(){
		redirect(uri:"/mainpage", absolute:true)
		
	}
	
	def userHome(){
		if(session.user)
			redirect(uri:"/functionalities", absolute:true)
		else{
			flash.message="You must log in first"
			redirect(uri:"/", absolute:true)
			return false
		}
	}
	def add() {
		def u= new User(username: params.username, name:(params.name+" "+params.lastname),
			 userType: params.userType, password:params.password )
		
		u.save()
		redirect(controller:"user" ,action:"index")
	}
	
	def delete(){
		def user = User.findByUsername(params.username)
		if (!user) {
        redirect(action:"list")
		}
		else{
		user.delete();
		redirect(controller:"user" ,action:"index")
	
		}
	}
	
	def edit(){
		def u= User.findByUsername(params.username)
		
	   	if (!u) {
        redirect(action:"index")
		}
		else{
			[user: u]
	
		}
	}
	
	def setValues(){
		def user = User.findByUsername(params.oldUsername)
		user.putAt("username", params.username)
		user.putAt("password", params.password)
		user.putAt("name", (params.name+" "+params.lastname))
		user.putAt("userType", params.userType)
		redirect(action:"index")
	}
	
	def login(){
		def user = User.findWhere(username:params.username,
			password:params.password)
		
		if (user){
			session.user = user
			flash.message=user.name
			redirect(uri: '/home', absolute: true)
		}
		else
		{
			flash.message="Wrong username or password"
			redirect(uri: '/', absolute: true)	
		}
	}
	
	def logout(){
		session.invalidate()
		redirect(uri: '/', absolute: true)
	}
	
	
}