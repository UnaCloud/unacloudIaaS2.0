package unacloud2

class HypervisorController {
	
	HypervisorService hypervisorService
	
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
		[hypervisors: Hypervisor.list(params)];
	}
	
	def add() {
		hypervisorService.addHypervisor(params.name, params.hyperVersion)
		redirect(action:"index")
	}
	
	def delete(){
		def hypervisor = Hypervisor.get(params.id)
		if (!hypervisor) {
        redirect(action:"list")
		}
		else{
			hypervisorService.deleteHypervisor(hypervisor)
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
		hypervisorService.setValues(hypervisor, params.name)
		redirect(action:"index")
	}
	
}
