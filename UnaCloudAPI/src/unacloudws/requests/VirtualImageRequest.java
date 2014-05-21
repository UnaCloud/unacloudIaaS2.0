package unacloudws.requests;

public class VirtualImageRequest {
	int instances,ram,cores,imageId;
	String hostname;
	public VirtualImageRequest(int instances, int ram, int cores, int imageId, String hostname) {
		this.instances = instances;
		this.ram = ram;
		this.cores = cores;
		this.imageId = imageId;
		this.hostname = hostname;
	}
	public int getCores() {
		return cores;
	}
	public int getInstances() {
		return instances;
	}
	public int getRam() {
		return ram;
	}
	public int getImageId() {
		return imageId;
	}
	public String getHostname() {
		return hostname;
	}
}
