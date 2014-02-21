package unacloud2

class UserController {

	UserService userService

	def beforeInterceptor = [action:{
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
		}, except: [
			'login',
			'logout',
			'home',
			'userHome',
			'account',
			'changePass',
			'refreshAPIKey'
		]]

	def index() {
		[users: User.list(params)];
	}

	def home(){
		if(!session.user){
			redirect(uri:"/login", absolute:true)
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
		userService.addUser(params.username, params.name+" "+params.lastname, params.userType,
				params.password )
		redirect(controller:"user" ,action:"index")
	}

	def account(){

		def u= User.get(session.user.id)
		if (!u) {
			redirect(action:"index")
		}
		else{
			[user: u]
		}
	}

	def setPolicy(){
		User u= User.findByUsername(params.username)
		userService.setPolicy(u, params.type,  params.value)
		redirect(action:"index")
	}

	def editPerms(){
		def u= User.findByUsername(params.username)
		if (!u) {
			redirect(action:"index")
		}
		else{
			def found
			if(params.data!=null){
				for(UserRestriction allocPolicy:u.restrictions){
					if(allocPolicy.name.equals(params.data)){
						found=true
						render "<input id=\"value\" name=\"value\" type=\"text\" value=\""+allocPolicy.getValue()+"\">"
					}
				}
				if(found!=true){
				render "<input id=\"value\" name=\"value\" type=\"text\" value=\"\">"
				}
			}
			else{
			[user: u]
			}
		}
	}

	def changePass(){
		def u= User.get(session.user.id)
		if (!u) {
			redirect(action:"index")
		}
		else{
			if(u.password.equals(params.oldPassword)){
				if(params.newPassword.equals(params.confirmPassword)){
					userService.changePass(u, params.newPassword)
					redirect(uri:"/", absolute:true)
				}
				else {
					flash.message="Passwords don't match"
					redirect(uri:"/account", absolute:true)
				}
			}
			else{
				flash.message="Incorrect Password"
				redirect(uri:"/account", absolute:true)
			}
		}
	}

	def refreshAPIKey(){
		def u= User.get(session.user.id)
		if (!u) {
			redirect(action:"index")
		}
		else{
			userService.refreshAPIKey(u)
			redirect (action:"account")
		}
	}

	def delete(){
		def user = User.findByUsername(params.username)
		if (!user) {
			redirect(action:"list")
		}
		else{
			userService.deleteUser(user)
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
		userService.setValues(user,params.username, params.name+" "+params.lastname, params.userType, params.password)
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
		else {
			flash.message="Wrong username or password"
			redirect(uri: '/login', absolute: true)
		}
	}

	def logout(){
		session.invalidate()
		redirect(uri: '/', absolute: true)
	}
}