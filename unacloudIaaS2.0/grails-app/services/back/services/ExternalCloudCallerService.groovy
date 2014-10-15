package back.services

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*

import grails.transaction.Transactional
import unacloud2.VirtualMachineImage

@Transactional
/**
 * Makes public cloud services calls using AWS API.
 * @author asistente
 *
 */
class ExternalCloudCallerService {
	
	AWSCredentials credentials
	AmazonEC2Client ec2
	
	def uploadImage(File f){
		initialize()		
		ImportInstanceRequest importInstanceRequest = new ImportInstanceRequest();
		DiskImage di
		ec2.importInstance(importInstanceRequest);
	}
	
	def runInstances(String imageId, int numberOfInstances){
		initialize()
		RunInstancesRequest runInstancesRequest= new RunInstancesRequest(imageId, numberOfInstances, numberOfInstances).withInstanceType("t1.micro");
		return ec2.runInstances(runInstancesRequest);
	}
	
	def terminateInstances(List<String> instanceIds){
		initialize()
		TerminateInstancesRequest terminateInstancesRequest= new TerminateInstancesRequest(instanceIds);
		ec2.terminateInstances(terminateInstancesRequest);
	}
	
	def describeImages(String ownerId){
		initialize()
		DescribeImagesRequest describeImagesRequest= new DescribeImagesRequest();
		describeImagesRequest.withOwners(ownerId);
		return ec2.describeImages(describeImagesRequest).getImages();
	}
	
	def describeInstances(){
		initialize()
		return ec2.describeInstances().getReservations().get(0).getInstances();
		
	}
	
	def initialize(){
		if(ec2== null){
			credentials = new ProfileCredentialsProvider().getCredentials();
			ec2 = new AmazonEC2Client(credentials)
			
		}
	}
	
	
    
}
