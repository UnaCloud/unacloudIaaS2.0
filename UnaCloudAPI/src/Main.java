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
		for(int e=0;e<100;e++){
			
		
		
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		JsonNode cluster=createClusterDeployJSONRequest(1,new VirtualMachineRequest(1,4,2,1),new VirtualMachineRequest(1,1,1,2));
	    WebResource resource = client.resource("http://157.253.202.170:8080/Unacloud2/WebServices");
	    // lets get the XML as a String
	    
	    	//System.out.println("Enviando "+mapper.writeValueAsString(rootNode));
			/*String text = resource.path("getClusterList").queryParam("login","admin").queryParam("apiKey", "UJNDE79XH5U0OE45VIRPLSD5GVEU0PWX").accept("application/text").post(String.class);
			System.out.println(text);
			JsonNode rootNode = mapper.readValue(text, JsonNode.class);*/
		    System.out.println(mapper.writeValueAsString(cluster));
		    String text = resource.path("startCluster").queryParam("login","admin").queryParam("apiKey","5Y95ZO4JVS5YI2EGFQLVT175RGF8QOVE").
		    		queryParam("cluster",mapper.writeValueAsString(cluster)).accept("application/text").post(String.class);
		    System.out.println(text);}
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
