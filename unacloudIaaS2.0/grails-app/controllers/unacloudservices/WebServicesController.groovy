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
		render new JSONObject().put("deploymentId",webServicesService.startCluster(login,apiKey,jsonCluster))
	}
	
	def getClusterList(String login,String apiKey){
		render webServicesService.getClusterList(login,apiKey) as JSON	
	}
	
	def startClusterMultipleOptions(String login,String apiKey, String cluster){
		JSONObject jsonCluster= new JSONObject(cluster)
		render new JSONObject().put("deploymentId",webServicesService.startClusterMultipleOptions(login,apiKey,jsonCluster))
	}
}
