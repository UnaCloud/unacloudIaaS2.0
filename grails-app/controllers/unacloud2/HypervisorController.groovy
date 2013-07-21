package unacloud2

class HypervisorController {
	
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
		[hypervisors: Hypervisor.list(params)];
	}
	
	def add() {
		def h= new Hypervisor(name:params.name )
		h.save()
		redirect(action:"index")
	}
	
	def delete(){
		def hypervisor = Hypervisor.get(params.id)
		if (!hypervisor) {
        redirect(action:"list")
		}
		else{
			
			hypervisor.delete()
			redirect(action:"index")
		}
	}
	
	def edit(){
		def h= Hypervisor.get(params.id)
		
	   	if (!h) {
			redirect(action:"index")
		}
		else{
			[hypervisor: h]
		}
	}
	
	def setValues(){
		def hypervisor = Hypervisor.get(params.id)
		hypervisor.putAt("name", params.name)
		redirect(action:"index")
	}
	
}
