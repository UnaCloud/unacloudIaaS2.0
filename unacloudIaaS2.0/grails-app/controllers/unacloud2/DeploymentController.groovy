package unacloud2

import java.util.regex.Pattern.Start;

import webutils.ImageRequestOptions;

class DeploymentController {

	DeploymentService deploymentService
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
			deploymentService.deployImage(image, user)
			redirect(action: "index")
		}
		else {
			flash.message= "Image already deployed"
			redirect(controller:"virtualMachineImage",action:"index")
		}
	}

	def deploy(){
		Cluster cluster= Cluster.get(params.id)
		User user= User.get(session.user.id)
		if(!cluster.isDeployed()){
			int totalInstances
			def imageNumber= cluster.images.size()
			if(imageNumber==1){
				totalInstances+= params.instances as Integer
			}
			else{
				for (int i=0; i< params.instances.size();i++) {
					totalInstances+=params.instances.getAt(i).toInteger()
				}
			}
			def avaliableInstances= PhysicalMachine.findAllByState("ON").size()
			if(totalInstances<=avaliableInstances){
				def temp=new ImageRequestOptions[cluster.images.size()];
				if(cluster.images.size()==1){
					temp[0]=new ImageRequestOptions(cluster.images.getAt(0).id, params.RAM.toInteger(),params.cores.toInteger(),params.instances.toInteger());
				}
				else{
					cluster.images.eachWithIndex {it,idx->
						temp[idx]=new ImageRequestOptions(it.id, params.RAM.getAt(idx).toInteger(),params.cores.getAt(idx).toInteger(),params.instances.getAt(idx).toInteger());
					}
				}
				deploymentService.deploy(cluster, user, params.time.toInteger(), temp)
				redirect(controller:"deployment")
			}
			else{
				flash.message="Instance limit exceeded"
				redirect( controller: "cluster",action: "deployOptions", id:cluster.id )
			}
		}
		else{
			flash.message="Cluster already deployed"
			redirect( controller: "cluster",action: "deployOptions", id:cluster.id )
		}
	}

	def history(){

		[deployments: session.user.deployments]
	}

	def stop(){
		def user= User.get(session.user.id)
		params.each {
			if (it.key.contains("hostname")){
				if (it.value.contains("on")){
					VirtualMachineExecution vm = VirtualMachineExecution.get((it.key - "hostname") as Integer)
					deploymentService.stopVirtualMachineExecution(vm)
				}
			}
		}
		deploymentService.stopDeployments(user)
		redirect(action:"index")
	}

	def addInstances(){
		def depImage=DeployedImage.get(params.id)
		def instance=params.instances.toInteger()
		deploymentService.addInstances(depImage, instance, params.time)
		redirect(action: "index")
	}
}
