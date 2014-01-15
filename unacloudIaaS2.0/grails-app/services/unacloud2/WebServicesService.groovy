package unacloud2

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import webutils.ImageRequestOptions;
import wsEntities.WebServiceException


class WebServicesService {
	DeploymentService deploymentService
    
	def startCluster(String login,String apiKey,JSONObject cluster) {
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login)
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		JSONArray images= cluster.getJSONArray("images")
		ImageRequestOptions[] options= new ImageRequestOptions[images.length()]
		for(int i=0; i<images.length();i++){
			JSONObject image=images.get(i)
			options[i]= new ImageRequestOptions(image.get("imageId"), image.get("ram"), image.get("cores"), image.get("instances"))			
		}
		def userCluster= Cluster.get(cluster.get("clusterId"))
		deploymentService.deploy(userCluster, user, options)
	}
	
	def getClusterList(String login,String apiKey){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login);
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		return user.userClusters
	}
}
