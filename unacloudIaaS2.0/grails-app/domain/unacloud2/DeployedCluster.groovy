package unacloud2

class DeployedCluster {
	Cluster cluster
	static hasMany = [images: DeployedImage, allocPolicies: AllocationPolicy]
    static constraints = {
		cluster nullable: true
    }
}
