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
	
	def startCluster(String cluster){
		JSONObject jsonCluster= new JSONObject(cluster)
		webServicesService.startCluster(cluster)
			
	}
	
	def getClusterList(String userData){
		JSONObject data= new JSONObject(userData)
		render webServicesService.getClusterList(data) as JSON	
	}
}
