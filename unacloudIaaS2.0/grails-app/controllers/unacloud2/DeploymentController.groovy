package unacloud2

import java.util.regex.Pattern.Start;

class DeploymentController {
	 
	DeploymentService depService
	def beforeInterceptor = {
		if(!session.user){
			
			flash.message="You must log in first"
			redirect(uri:"/login", absolute:true)
			return false
		}
		session.user.refresh()
	}
	
    def index() { 
		[deployments: session.user.getActiveDeployments()]
	}
	def addInstancesOptions(){
		[id:params.id]
	}
	
	def deployImage(){
		
		def image= VirtualMachineImage.get(params.id)
		def user= User.get(session.user.id)
		if (!image.isDeployed()){
			depService.deployImage(image, user)
			redirect(action: "index")
		}
		else
		{
			flash.message= "Image already deployed"
			redirect(controller:"virtualMachineImage",action:"index")
		}
	}
	
	def deploy(){
		
		Cluster cluster= Cluster.get(params.id)
		User user= User.get(session.user.id)
		if(!cluster.isDeployed()){
			depService.deploy(cluster, user, params.instances, params.ram, params.cores, params.time)
			redirect(controller:"deployment")
		}
		else{
			flash.message="Cluster already deployed"
			redirect( controller: "cluster",action: "index" )
		}
	
	}
	
	def history(){
		
		[deployments: session.user.deployments]
	}
	
	def stop(){
		params.each {
			if (it.key.contains("hostname")){
				if (it.value.contains("on")){
					VirtualMachineExecution vm = VirtualMachineExecution.get((it.key - "hostname") as Integer)
					depService.stopVirtualMachineExecution(vm)
					
				}
			}
		}
		depService.stopDeployments()
		redirect(action:"index")
	}
	
	def addInstances(){
		def depImage=DeployedImage.get(params.id)
		def instance=params.instances.toInteger()
		depService.addInstances(depImage, instance, params.time)
		redirect(action: "index")
	}
}
