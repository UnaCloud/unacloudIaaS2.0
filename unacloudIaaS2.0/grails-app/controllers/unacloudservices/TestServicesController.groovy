package unacloudservices

import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import unacloud2.User;

@Transactional(readOnly = true)
class TestServicesController{
	static responseFormats = ['json', 'xml']

    def index(int vmSize) {
		render "holamundo";
		respond User.list();
	}
	
	def startCluster(JSONObject Cluster){
		//TODO
	}
}
