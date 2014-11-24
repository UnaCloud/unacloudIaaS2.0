package unacloud2

class ClusterService {
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------
	
	/**
	 * Creates a new cluster with the given parameters
	 * @param images Virtual machine images belonging to new cluster
	 * @param cluster empty new cluster
	 * @param user cluster owner
	 */
	
    def saveCluster(images, Cluster cluster, User user) {
		cluster.images=[]
		if(images.getClass().equals(String)){
			cluster.images.add(VirtualMachineImage.get(images))
		}
		else{
			for(image in images){
				cluster.images.add(VirtualMachineImage.get(image))
			}
		}
		cluster.save(failOnError: true)
		if(user.userClusters==null)
			user.userClusters
		user.userClusters.add(cluster)
		user.save(failOnError: true)
    }
	
	/**
	 * Deletes the cluster given as parameter
	 * @param cluster cluster to be deleted
	 * @param user cluster owner
	 */
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
