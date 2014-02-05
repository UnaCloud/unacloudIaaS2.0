package unacloudservices

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.transaction.annotation.Transactional;

import unacloud2.WebServicesService;

class WebServicesController{
	static responseFormats = ['json', 'xml']
	
	WebServicesService webServicesService
	
	def index(int vmSize) {
		render "holamundo";
	}
	
	def startCluster(String login,String apiKey,String cluster){
		JSONObject jsonCluster= new JSONObject(cluster)
		render webServicesService.startCluster(login,apiKey,jsonCluster) as JSON
	}
	
	def startHeterogeneousCluster(String login,String apiKey, String cluster){
		JSONObject jsonCluster= new JSONObject(cluster)
		render webServicesService.startHeterogeneousCluster(login,apiKey,jsonCluster) as JSON
	}
	
	def getClusterList(String login,String apiKey){
		render webServicesService.getClusterList(login,apiKey) as JSON	
	}
	
	def stopVirtualMachine(String login, String apiKey, String machineId){
		render webServicesService.stopVirtualMachine(login,apiKey,machineId) 
	}
	
	def stopDeployment(String login, String apiKey, String depId){
		render webServicesService.stopDeployment(login,apiKey,depId)
	}

	def restart(){
			
	}
	
	def getActiveDeployments(String login,String apiKey){
		render webServicesService.getActiveDeployments(login,apiKey) as JSON
	}
	
	def getDeploymentInfo(String login,String apiKey, String depId){
		render webServicesService.getDeploymentInfo(login,apiKey,depId) as JSON
	}
	
	def changeAllocationPolicy(String login,String apiKey, String allocationPolicy){
		render webServicesService.changeAllocationPolicy(login,apiKey,allocationPolicy)		
	}
	
	def addInstances(String login,String apiKey, String imageId,int instances,int time){
		render webServicesService.addInstances(login,apiKey,imageId,instances,time)
	}
}
