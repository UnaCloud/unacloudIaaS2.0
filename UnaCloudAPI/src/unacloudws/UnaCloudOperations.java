/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unacloudws;

import java.util.List;

import unacloudws.requests.VirtualMachineRequest;
import unacloudws.responses.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class UnaCloudOperations {
	static ObjectMapper mapper=new ObjectMapper();
	String username;
    String apiKey;
    public UnaCloudOperations(String username, String password) {
        this.username = username;
        this.apiKey = password;
    }
    
    
    public List<VirtualMachineExecutionWS> getVirtualMachineExecutions(int templateID) {
        /*UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getVirtualMachineExecutions(username, apiKey, templateID);*/
    	return null;
    }

    public Integer getAvailableVirtualMachines(int templateSelected, int virtualMachineDisk, int virtualMachineCores, int virtualMachineRAM) {
        /*UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getAvailableVirtualMachines(templateSelected, virtualMachineDisk, virtualMachineCores, virtualMachineRAM, username, apiKey);*/
    	return null;
    }

    public String startVirtualCluster(int clusterId, long time,VirtualMachineRequest...vms) {
    	JsonNode cluster = mapper.createObjectNode();
    	ObjectNode clusterON=((ObjectNode) cluster);
    	clusterON.put("clusterId", clusterId);
    	clusterON.put("execTime", time);
    	clusterON.put("images", mapper.valueToTree(vms));
    	try {
			return prepareRequest("startCluster").queryParam("cluster",mapper.writeValueAsString(cluster)).post(String.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
    }

    public String stopVirtualMachine(String virtualMachineExID) {
        /*UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.turnOffVirtualMachine(username, apiKey, virtualMachineExID);*/
    	return null;
    }
    public List<TemplateWS> getClusterList() {
        /*UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getTemplateLists(username, apiKey);*/
    	return null;
    }

    public static Integer getTotalUnaCloudResources(int machineDisk, int machineCores, int machineRam) {
        /*UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getTotalUnaCloudResources(machineDisk, machineCores, machineRam);*/
    	return null;
    }

    public static Integer getAvailableUnaCloudResources(int machineDisk, int machineCores, int machineRam) {
        /*UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getAvailableUnaCloudResources(machineDisk, machineCores, machineRam);*/
    	return null;
    }

    public Integer getTotalVirtualMachines(int machineDisk, int machineCores, int machineRam, int templateCode) {
        /*UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getTotalVirtualMachines(machineDisk, machineCores, machineRam, templateCode, username, apiKey);*/
    	return null;
    }

    public Integer getBusyUnaCloudResources(int machineDisk, int machineCores, int machineRam) {
        /*UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getBusyUnaCloudResources(machineDisk, machineCores, machineRam);*/
    	return null;
    }
    public String changeAllocationPolicy(String newPolicy){
    	return prepareRequest("changeAllocationPolicy").queryParam("allocationPolicy", newPolicy).post(String.class);
    }
    
    private WebResource prepareRequest(String serviceName){
    	DefaultClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		WebResource resource = client.resource("http://localhost:8080/unacloudIaaS2.0/WebServices");
		return resource.path(serviceName).queryParam("login",username).queryParam("apiKey",apiKey);
    }
    /*public String doRequest(String serviceName,JsonNode cluster){
    	UnaCloudOperations ops=new UnaCloudOperations("admin","3NA4ABDVBQRWDYD9TIY0HVW5V9XYYQEA");
		ops.startVirtualCluster(1,60,new VirtualMachineRequest(1,4,2,1),new VirtualMachineRequest(1,1,1,1),new VirtualMachineRequest(2,2,2,1));
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		
		//JsonNode cluster=createClusterDeployJSONRequest();
	    WebResource resource = client.resource("http://localhost:8080/unacloudIaaS2.0/WebServices");
	    // lets get the XML as a String
	    
	    	//System.out.println("Enviando "+mapper.writeValueAsString(rootNode));
			//String text = resource.path("getClusterList").queryParam("login","admin").queryParam("apiKey", "UJNDE79XH5U0OE45VIRPLSD5GVEU0PWX").accept("application/text").post(String.class);
			//System.out.println(text);
			//JsonNode rootNode = mapper.readValue(text, JsonNode.class);
		    try{
		    	System.out.println(mapper.writeValueAsString(cluster));
		    }catch(Exception ex){
		    	
		    }
//		    String text = resource.path("startCluster").queryParam("login","admin").queryParam("apiKey","3NA4ABDVBQRWDYD9TIY0HVW5V9XYYQEA").
//		    		queryParam("cluster",mapper.writeValueAsString(cluster)).accept("application/text").post(String.class);

//		    String text = resource.path("stopCluster").queryParam("login","admin").queryParam("apiKey","3NA4ABDVBQRWDYD9TIY0HVW5V9XYYQEA").
//		    		queryParam("cluster","28").accept("application/text").post(String.class);
//		 
		    
//		    String text = resource.path("getActiveDeployments").queryParam("login","admin").queryParam("apiKey","3NA4ABDVBQRWDYD9TIY0HVW5V9XYYQEA").post(String.class);
		 
//		    String text = resource.path("getDeploymentInfo").queryParam("login","admin").queryParam("apiKey","3NA4ABDVBQRWDYD9TIY0HVW5V9XYYQEA").queryParam("depId", "15").post(String.class);
	    return "";
    }*/

}
