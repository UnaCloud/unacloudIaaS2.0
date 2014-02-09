package unacloudws.requests;

public class VirtualImageRequest {
	int instances,ram,cores,imageId;
	public VirtualImageRequest(int instances, int ram, int cores, int imageId) {
		this.instances = instances;
		this.ram = ram;
		this.cores = cores;
		this.imageId = imageId;
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
}
