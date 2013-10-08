package unacloud2

class OperatingSystemController {
	
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
		[oss: OperatingSystem.list(params)];
	}
	
	def add() {
		def o= new OperatingSystem(name:params.name , configurer:params.configurer )
		o.save()
		redirect(action:"index")
	}
	
	def delete(){
		def os = OperatingSystem.get(params.id)
		if (!os) {
        redirect(action:"list")
		}
		else{
			os.delete()
			redirect(action:"index")
		}
	}
	
	def edit(){
		def o= OperatingSystem.get(params.id)
		
	   	if (!o) {
			redirect(action:"index")
		}
		else{
			[os: o]
		}
	}
	
	def setValues(){
		def os = OperatingSystem.get(params.id)
		os.putAt("name", params.name)
		os.putAt("configurer", params.configurer)
		redirect(action:"index")
	}
	
}
