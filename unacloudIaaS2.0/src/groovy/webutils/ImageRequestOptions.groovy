package webutils

class ImageRequestOptions {

	int ram
	int cores	int instances 
	long imageId
	String hostname
	
	public ImageRequestOptions(long imageId,int ram,int cores, int instances, String hostname) {
		this.imageId = imageId;
		this.ram = ram
		this.cores= cores
		this.instances = instances
		this.hostname = hostname
	}
	public ImageRequestOptions() {
		
	}
	@Override
	public String toString() {
		return "ImageRequestOptions [ram=" + ram + ", cores=" + cores
				+ ", instances=" + instances + "]";
	}
	
	
}
