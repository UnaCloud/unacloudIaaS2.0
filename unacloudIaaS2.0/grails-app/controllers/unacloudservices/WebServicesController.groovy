package unacloudservices

import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
class WebServicesController{
	static responseFormats = ['json', 'xml']

    def index(int vmSize) {
		render "holamundo";
	}
	
	def startCluster(JSONObject Cluster){
		//TODO
		
	}
}
