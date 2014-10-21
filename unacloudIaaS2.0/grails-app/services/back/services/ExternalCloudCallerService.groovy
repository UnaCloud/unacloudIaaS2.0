package back.services

import java.nio.charset.StandardCharsets;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*

import grails.transaction.Transactional
import unacloud2.ExternalCloudAccount;
import unacloud2.ServerVariable;
import unacloud2.VirtualMachineImage

@Transactional
/**
 * Makes public cloud services calls using AWS API.
 * @author asistente
 *
 */
class ExternalCloudCallerService {
	
	AWSCredentials credentials
	AmazonEC2Client endpoint
	
	def uploadImage(File f){
		initializeStorage()		
		ImportInstanceRequest importInstanceRequest = new ImportInstanceRequest()
		DiskImage di
		endpoint.importInstance(importInstanceRequest)
	}
	
	def runInstances(String imageId, int numberOfInstances){
		initializeComputing()
		RunInstancesRequest runInstancesRequest= new RunInstancesRequest(imageId, numberOfInstances, numberOfInstances).withInstanceType("t1.micro")
		return endpoint.runInstances(runInstancesRequest)
	}
	
	def terminateInstances(List<String> instanceIds){
		initializeComputing()
		TerminateInstancesRequest terminateInstancesRequest= new TerminateInstancesRequest(instanceIds)
		return endpoint.terminateInstances(terminateInstancesRequest).getTerminatingInstances()
	}
	
	def describeImages(String ownerId){
		initializeComputing()
		DescribeImagesRequest describeImagesRequest= new DescribeImagesRequest()
		describeImagesRequest.withOwners(ownerId)
		return endpoint.describeImages(describeImagesRequest).getImages()
	}
	
	def describeInstance(Collection<String> instanceIds){
		initializeComputing()
		if (instanceIds.isEmpty()) return new ArrayList<Instance>()
		DescribeInstancesRequest dir= new DescribeInstancesRequest().withInstanceIds(instanceIds)
		return endpoint.describeInstances(dir).getReservations().get(0).getInstances()
		
	}
	
	def initializeComputing(){
		ExternalCloudAccount account
		try{
		String accountName= ServerVariable.findByName('EXTERNAL_COMPUTING_ACCOUNT').getVariable()
		account= ExternalCloudAccount.findByName(accountName)
		}
		catch(Exception e){
			throw new Exception("Invalid external computing account variable")
		}
		String accountCredentials= "accessKey="+account.account_id+System.getProperty("line.separator")+"secretKey="+account.account_key
		InputStream is= new ByteArrayInputStream(accountCredentials.getBytes(StandardCharsets.UTF_8));
		
		credentials = new PropertiesCredentials(is)
		endpoint = new AmazonEC2Client(credentials)
	}
	
	def initializeStorage(){
		
	}
	
    
}
