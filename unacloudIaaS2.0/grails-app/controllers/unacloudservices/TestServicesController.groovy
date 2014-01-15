package unacloudservices

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.transaction.annotation.Transactional;

import unacloud2.User;
import wsEntities.WebServiceException

@Transactional(readOnly = true)
class TestServicesController{
	static responseFormats = ['json', 'xml']

    def index() {
		response.setContentType("application/json")
		render  new WebServiceException("Perro") as JSON;
	}
	
	def startCluster(String cluster){
		
		println new JSONObject(cluster)
		render  new WebServiceException("Perro") as JSON;
	}
}
