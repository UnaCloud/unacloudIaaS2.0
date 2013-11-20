package unacloud2

class ClusterController {
	
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
		cluster.images=[]
		if(params.images.getClass().equals(Long))
		cluster.images.add(VirtualMachineImage.get(params.images))
		else{
		for(image in params.images){
			cluster.images.add(VirtualMachineImage.get(image))
		}
		}
		cluster.save()
		def user= User.get(session.user.id)
		if(user.userClusters==null)
			user.userClusters
		user.userClusters.add(cluster)
		user.save()
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
			user.userClusters.remove(cluster)
			user.save()
			cluster.delete()
			redirect(action:"index")
		}
	}
		
}
