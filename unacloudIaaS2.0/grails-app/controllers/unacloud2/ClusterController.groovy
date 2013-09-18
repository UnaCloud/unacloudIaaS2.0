package unacloud2

class ClusterController {
	
	def beforeInterceptor = {
		if(!session.user){
			
			flash.message="You must log in first"
			redirect(uri:"/", absolute:true)
			return false
		}
		session.user.refresh()
	}
	
    def index() { 
		[clusters: session.user.userClusters]
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
		[images: Cluster.get(params.id).getOrderedImages(), limit: 20, limitHA: 2]	
	}
	
	def deploy(){
		
	}
}
