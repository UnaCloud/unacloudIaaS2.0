import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class Main {
	static ObjectMapper mapper=new ObjectMapper();
	public static void main(String[] args)throws Exception{
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		JsonNode cluster=createClusterDeployJSONRequest(1,new VirtualMachineRequest(1,4,2,1),new VirtualMachineRequest(1,1,1,1),new VirtualMachineRequest(2,2,2,1));
	    WebResource resource = client.resource("http://localhost:8080/unacloudIaaS2.0/WebServices");
	    // lets get the XML as a String
	    
	    	//System.out.println("Enviando "+mapper.writeValueAsString(rootNode));
			/*String text = resource.path("getClusterList").queryParam("login","admin").queryParam("apiKey", "UJNDE79XH5U0OE45VIRPLSD5GVEU0PWX").accept("application/text").post(String.class);
			System.out.println(text);
			JsonNode rootNode = mapper.readValue(text, JsonNode.class);*/
		    System.out.println(mapper.writeValueAsString(cluster));
		    String text = resource.path("startClusterMultipleOptions").queryParam("login","admin").queryParam("apiKey","3NA4ABDVBQRWDYD9TIY0HVW5V9XYYQEA").
		    		queryParam("cluster",mapper.writeValueAsString(cluster)).accept("application/text").post(String.class);
		    System.out.println(text);
	}
	public static JsonNode createClusterDeployJSONRequest(int clusterId,VirtualMachineRequest...vms)throws Exception{
		JsonNode rootNode = mapper.createObjectNode();
		((ObjectNode) rootNode).put("clusterId", clusterId);
		((ObjectNode) rootNode).put("execTime", 60);
		((ObjectNode) rootNode).put("images", mapper.valueToTree(vms));
	    return rootNode;
	}
	static class VirtualMachineRequest{
		int instances,ram,cores,imageId;
		public VirtualMachineRequest(int instances, int ram, int cores, int imageId) {
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
}
