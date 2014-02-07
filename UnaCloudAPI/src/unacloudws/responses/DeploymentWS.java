package unacloudws.responses;

public class DeploymentWS {
	String id;
	ClusterWS cluster;
	public DeploymentWS(String id, ClusterWS cluster) {
		this.id = id;
		this.cluster = cluster;
	}
	public String getId() {
		return id;
	}
	public ClusterWS getCluster() {
		return cluster;
	}
}
