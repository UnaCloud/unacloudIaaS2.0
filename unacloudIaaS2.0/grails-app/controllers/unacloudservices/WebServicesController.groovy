package unacloudservices

import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import unacloud2.WebServicesService;

@Transactional(readOnly = true)
class WebServicesController{
	static responseFormats = ['json', 'xml']
	
	WebServicesService webServicesService
    def index(int vmSize) {
		render "holamundo";
	}
	
	def startCluster(JSONObject cluster){
		webServicesService.startCluster(cluster)
			
	}
	
	def getClusterList(JSONObject API_key){
		return webServicesService.getClusterList(API_key)	
	}
}
