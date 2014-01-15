package unacloudservices

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.transaction.annotation.Transactional;

import unacloud2.WebServicesService;

@Transactional(readOnly = true)
class WebServicesController{
	static responseFormats = ['json', 'xml']
	
	WebServicesService webServicesService
    
	def index(int vmSize) {
		render "holamundo";
	}
	
	def startCluster(String login,String apiKey,String cluster){
		JSONObject jsonCluster= new JSONObject(cluster)
		webServicesService.startCluster(login,apiKey,jsonCluster)
	}
	
	def getClusterList(String login,String apiKey){
		render webServicesService.getClusterList(login,apiKey) as JSON	
	}
}
