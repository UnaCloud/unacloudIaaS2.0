package unacloud2

import webutils.ImageRequestOptions;

class ClusterController {
	
	ClusterService clusterService
	
	def beforeInterceptor = {
		if(!session.user){
			
			flash.message="You must log in first"
			redirect(uri:"/login", absolute:true)
			return false
		}
		session.user.refresh()
	}
	
    def index() { 
		[clusters: session.user.getOrderedClusters()]
	}
	
	def newCluster(){
		ArrayList<VirtualMachineImage> images = session.user.getOrderedImages()
		VirtualMachineImage.all.each {
			if(it.isPublic&&!(it.user.equals(session.user))){
				images.add(it)
			}
		}
		[images: images]
	}
	
	def save(){
		def cluster = new Cluster( name: params.name)
		def user = User.get(session.user.id)
		clusterService.saveCluster(params.images, cluster, user)
		redirect(action: 'index')
	}
	
	def deployOptions(){
		def cluster=Cluster.get(params.id);
		[cluster: cluster,limit: PhysicalMachine.count, limitHA: 2]
	}
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
