package unacloud2

import back.userRestrictions.UserRestrictionProcessorService

import java.util.regex.Pattern.Start;

import webutils.ImageRequestOptions;

class DeploymentController {
	
	//-----------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------
	
	/**
	 * Representation of user restriction services
	 */
	
	UserRestrictionProcessorService userRestrictionProcessorService
	
	/**
	 * Representation of deployment services
	 */
	
	DeploymentService deploymentService
	
	//-----------------------------------------------------------------
	// Actions
	//-----------------------------------------------------------------
	
	/**
	 * Makes session verifications before executing any other action
	 */
	
	def beforeInterceptor = {
		if(!session.user){

			flash.message="You must log in first"
			redirect(uri:"/login", absolute:true)
			return false
		}
		session.user.refresh()
		deploymentService.stopDeployments(User.get(session.user.id))
	}
	
	/**
	 * Deployment index action. Controls view all function 
	 * @return deployments that must be shown according to view all checkbox
	 */
	
	def index() {
		if(params.viewAll==null || params.viewAll=="false" ){
			[deployments: session.user.getActiveDeployments(), checkViewAll: false]
		}
		else if(params.viewAll=="true"){
			List deployments= new ArrayList()
			def deps=Deployment.findByStatus('ACTIVE')
			for (dep in deps){
			if(dep.isActive())
				deployments.add(dep)
			}
			[deployments: deployments,checkViewAll: true]
		}
	}
	
	/**
	 * add instances action. it passes the image id
	 * @return id of the image in order to add instances
	 */
	def addInstancesOptions(){
		[id:params.id]
	}
	
	/**
	 * Deploys a single image for edit purposes
	 * @return
	 */
	
	def deployImage(){

		def image= VirtualMachineImage.get(params.id)
		def user= User.get(session.user.id)
		if (!image.isDeployed()){
			try{
			deploymentService.deployImage(image, user)
			}
			catch (Exception e){
				flash.message=e.message
				redirect(action: "error");
			}
			redirect(action: "index")
		}
		else {
			flash.message= "Image already deployed"
			redirect(controller:"virtualMachineImage",action:"index")
		}
	}
	
	/**
	 * Deploy action. Checks image number and depending on it, forms the parameters in 
	 * order to pass them to the service layer. It also catches exceptions and pass 
	 * them to error view. If everything works it redirects to index view.
	 */
	
	def deploy(){
		Cluster cluster= Cluster.get(params.get('id'))
		int totalInstances
			def user= User.get(session.user.id)
			def imageNumber= cluster.images.size()
			if(imageNumber==1){
				totalInstances= params.instances.toInteger()
			}
			else{
				for (int i=0; i< params.instances.size();i++) {
					totalInstances+=params.instances.getAt(i).toInteger()
				}
			}
			
			def temp=new ImageRequestOptions[cluster.images.size()];
			def highAvail= new boolean[cluster.images.size()]
			println "params: "+params
			if(imageNumber==1){
				temp[0]=new ImageRequestOptions(cluster.images.first().id, params.RAM.toInteger(),params.cores.toInteger(),params.instances.toInteger(),params.hostname);
				highAvail[0]= (params.get('highAvailability'+cluster.images.first().id))!=null
			}
			else{
				cluster.images.eachWithIndex {it,idx->
					highAvail[idx]=(params.get('highAvailability'+it.id))!=null
					temp[idx]=new ImageRequestOptions(it.id, params.RAM.getAt(idx).toInteger(),params.cores.getAt(idx).toInteger(),params.instances.getAt(idx).toInteger(), params.hostname.getAt(idx));
			}
			}
			println highAvail
			try{
			deploymentService.deploy(cluster, user, params.time.toLong()*60*60*1000, temp, highAvail)
			}
			catch(Exception e){
				if(e.getMessage()==null)
				flash.message= e.getCause()
				else
				flash.message= e.getMessage()
				redirect( uri: "/error",absolute: true )
				return
			}
			redirect(controller:"deployment")	
	}
	
	/**
	 * History action. Returns all data of every deployment
	 * @return deployments list
	 */
	
	def history(){

		[deployments: session.user.deployments]
	}
	
	/**
	 * Stop execution action. All nodes selected on the deployment interface will be
	 * stopped. Redirects to index when the operation is finished.
	 */
	
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
	
	/**
	 * Adds and executes new nodes to the selected deployed image. Redirects to 
	 * index when finished
	 */

	def addInstances(){
		def depImage=DeployedImage.get(params.id)
		def instance=params.instances.toInteger()
		User user= User.get(session.user.id)
		try{
		deploymentService.addInstances(depImage, user,instance, params.time.toLong()*60*60*1000)
		}
		catch (Exception e){
			if(e.getMessage()==null)
			flash.message= e.getCause()
			else
			flash.message= e.getMessage()
			redirect(uri:"/error", absolute:true)
			return
		}
		redirect(action: "index")
	}
}
