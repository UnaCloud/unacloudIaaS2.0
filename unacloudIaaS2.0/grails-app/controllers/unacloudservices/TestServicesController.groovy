package unacloudservices

import grails.converters.JSON
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import unacloud2.User;
import wsEntities.HolaMundo

@Transactional(readOnly = true)
class TestServicesController{
	static responseFormats = ['json', 'xml']

    def index() {
		response.setContentType("application/json")
		render  new HolaMundo("Perro") as JSON;
	}
	
	def startCluster(JSONObject Cluster){
		//TODO
	}
}
