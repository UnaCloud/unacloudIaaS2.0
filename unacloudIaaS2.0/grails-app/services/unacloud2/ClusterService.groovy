package unacloud2

class ClusterService {

    def saveCluster(images, Cluster cluster, User user) {
		cluster.images=[]
		if(images.getClass().equals(Long))
		cluster.images.add(VirtualMachineImage.get(images))
		else{
		for(image in images){
			cluster.images.add(VirtualMachineImage.get(image))
		}
		}
		cluster.save()
		if(user.userClusters==null)
			user.userClusters
		user.userClusters.add(cluster)
		user.save()
    }
	
	def deleteCluster(Cluster cluster, User user){
		for (depCluster in DeployedCluster.getAll()){
			if(depCluster.cluster!= null){
			if (depCluster.cluster.equals(cluster)){
				depCluster.cluster=null
				depCluster.save()
			}
			}
		}
		
		user.userClusters.remove(cluster)
		user.save()
		cluster.delete()	
	}
}
