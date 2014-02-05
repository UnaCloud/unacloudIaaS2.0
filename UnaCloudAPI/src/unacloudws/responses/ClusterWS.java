package unacloudws.responses;

import java.util.List;

public class ClusterWS {
	long id;
	String name;
	List<ImageWS> images;
	public ClusterWS(long id, String name, List<ImageWS> images) {
		this.id = id;
		this.name = name;
		this.images = images;
	}
	
	public ClusterWS(long id, String name) {
		this.id = id;
		this.name = name;
	}
}
