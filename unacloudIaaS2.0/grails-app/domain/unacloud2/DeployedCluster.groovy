package unacloud2

class DeployedCluster {
	Cluster cluster
	static hasMany = [images: DeployedImage]
    static constraints = {
		cluster nullable: true
    }
	static mapping = {
		sort 'cluster_id':'desc'
	}
}
