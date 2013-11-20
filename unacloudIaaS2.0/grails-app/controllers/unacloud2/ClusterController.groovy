package unacloud2

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
		[images: session.user.getOrderedImages()]
	}
	
	def save(){
		def cluster = new Cluster( name: params.name)
		def user = User.get(id)
		clusterService.saveCluster(params.images, cluster, user)
		redirect(action: 'index')
	}
	
	def deployOptions(){
		[cluster: Cluster.get(params.id),limit: 20, limitHA: 2]	
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
