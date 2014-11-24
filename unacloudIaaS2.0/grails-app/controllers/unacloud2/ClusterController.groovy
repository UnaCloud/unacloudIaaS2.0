package unacloud2

import webutils.ImageRequestOptions;

class ClusterController {
	
	//-----------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------
	
	/**
	 * Representation of cluster services
	 */
	
	ClusterService clusterService
	
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
	}
	
	/**
	 * Index action that sends cluster lists
	 * @return ordered clusters of the session user
	 */
    def index() { 
		[clusters: session.user.getOrderedClusters()]
	}
	
	/**
	 * New cluster creation action that sends the available images for the user
	 * @return list of ordered images that user can add to a new cluster
	 */
	def newCluster(){
		ArrayList<VirtualMachineImage> images = session.user.getOrderedImages()
		VirtualMachineImage.all.each {
			if(it.isPublic&&!(images.contains(it))){
				images.add(it)
			}
		}
		[images: images]
	}
	
	/**
	 * Save action of a new cluster. It redirects to index after saving  
	 */
	def save(){
		
		def cluster = new Cluster( name: params.name)
		def user = User.get(session.user.id)
		clusterService.saveCluster(params.images, cluster, user)
		redirect(action: 'index')
	}
	
	/**
	 * Deploy options action that brings the form with deploying options for each 
	 * image
	 * @return limits shown in the information of form and cluster to be deployed
	 */
	def deployOptions(){
		def cluster=Cluster.get(params.id);
		int limit
		int limitHA
		def machines=PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON)
		for (machine in machines)
		{
			if(machine.highAvailability)
			limitHA++
			else
			limit++
		}
		[cluster: cluster,limit: limit, limitHA: limitHA, hardwareProfiles: HardwareProfile.list()]
		
	}
	
	def externalDeployOptions(){
		def cluster=Cluster.get(params.id);
		for (image in cluster.images){
			if(image.externalId==null){
				flash.message= "One or more images in this cluster are not uploaded on the external provider"
				redirect( uri: "/error",absolute: true )
			}
		}
		ServerVariable computingAccount= ServerVariable.findByName('EXTERNAL_COMPUTING_ACCOUNT')
		ExternalCloudAccount account
		if(computingAccount!=null &&  !(computingAccount.variable.equals('None'))){
			account = ExternalCloudAccount.findByName(computingAccount.variable)
			[account:account,cluster: cluster, hardwareProfiles: HardwareProfile.findByProvider(account.provider)]
		
		}
		else{
			flash.message= "There isn't any configured account for external deployments"
			redirect( uri: "/error",absolute: true )
		}
		
		
	}
	/**
	 * Delete cluster action. Receives the cluster id in the params map and 
	 * redirects to index after deletion  
	 * @return
	 */
	def delete(){
		def cluster = Cluster.get(params.id)
		if (!cluster) {
		redirect(action:"index")
		}
		else if (cluster.isDeployed()) {
		redirect(action:"index")
		}
		else{
			def user= User.get(session.user.id)
			clusterService.deleteCluster(cluster, user)
			redirect(action:"index")
		}
	}
		
}
