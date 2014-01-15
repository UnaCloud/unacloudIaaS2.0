package webutils

class ImageRequestOptions {

	int ram
	int cores	int instances 
	long imageId;
	public ImageRequestOptions(long imageId,int ram,int cores, int instances) {
		this.imageId = imageId;
		this.ram = ram
		this.cores= cores
		this.instances = instances
	}
	public ImageRequestOptions() {
		
	}
	@Override
	public String toString() {
		return "ImageRequestOptions [ram=" + ram + ", cores=" + cores
				+ ", instances=" + instances + "]";
	}
	
	
}
