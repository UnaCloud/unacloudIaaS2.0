package AWSServices

import grails.converters.XML
import groovy.xml.MarkupBuilder

import javax.ws.rs.*
import javax.ws.rs.core.*;

import back.services.AWSService;
import unacloud2.User;
import unacloud2.VirtualMachineImageService;

@Path('/api/AWS')
class AWSResource {
	
	AWSService AWSService
	VirtualMachineImageService virtualMachineImageService
	
    @POST
	@Consumes('application/x-www-form-urlencoded')
	@Produces('application/xml')
    String getAWSRepresentation(MultivaluedMap<String, String> formParams, @FormParam("unused") String unused) {
		
		LinkedList action= formParams.get('Action')
		if(action==null){
			println 'no action param received'
			AWSService.sendAWSErrorResponse("InvalidAction", "The action urn:Post is not valid for this web service.")
		}
		else{
			switch(action.getFirst()){
				case "DescribeImages": 
					return AWSService.describeImages(formParams)
				case "DescribeInstances":
					return AWSService.describeInstances(formParams)
				case "DescribeRegions":
					return AWSService.describeRegions(formParams)
				case "TerminateInstances":
					return AWSService.terminateInstances(formParams)
				case "RebootInstances":
					return AWSService.rebootInstances(formParams)
				case "RunInstances":
					return AWSService.runInstances(formParams)
				
				default: 
				AWSService.sendAWSErrorResponse("InvalidAction", "The action "+action+" is not valid for this web service.")
			}
			
		}	
    }
	
	
}
