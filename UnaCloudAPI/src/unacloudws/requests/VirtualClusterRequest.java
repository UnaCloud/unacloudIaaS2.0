package unacloudws.requests;

public class VirtualClusterRequest {

	long clusterId;
	long time;
	VirtualImageRequest[]vms;
	public VirtualClusterRequest(long clusterId, long time,VirtualImageRequest...vms) {
		this.clusterId = clusterId;
		this.time = time;
		this.vms = vms;
	}
	public long getClusterId() {
		return clusterId;
	}
	public long getTime() {
		return time;
	}
	public VirtualImageRequest[] getVms() {
		return vms;
	}
	
}
