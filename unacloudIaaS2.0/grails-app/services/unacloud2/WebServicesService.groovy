package unacloud2

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import webutils.ImageRequestOptions;
import wsEntities.WebServiceException


class WebServicesService {
	DeploymentService deploymentService
	def startCluster(String login,String apiKey,JSONObject cluster) {
		println login+":"+apiKey+":"+cluster
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login)
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		JSONArray images= cluster.getJSONArray("images")
		ImageRequestOptions[] options= new ImageRequestOptions[images.length()]
		for(int i=0; i<images.length();i++){
			JSONObject image=images.get(i)
			options[i]= new ImageRequestOptions(image.get("imageId"), image.getInt("ram"), image.getInt("cores"), image.getInt("instances"))
		}
		def userCluster= Cluster.get(cluster.get("clusterId"))
		return deploymentService.deploy(userCluster, user, (Long)cluster.getInt("execTime")*60000,options)
	}

	def stopCluster(String login,String apiKey,String clusterId){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login)
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		DeployedCluster cluster= DeployedCluster.get(clusterId)
		for(image in cluster.images){
			for(vm in image.virtualMachines){
				deploymentService.stopVirtualMachineExecution(vm)	
			}
		}
		return "Success"
		
	}

	def startClusterMultipleOptions(String login,String apiKey,JSONObject cluster) {
		println login+":"+apiKey+":"+cluster
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login)
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		JSONArray images= cluster.getJSONArray("images")
		ImageRequestOptions[] options= new ImageRequestOptions[images.length()]
		for(int i=0; i<images.length();i++){
			JSONObject image=images.get(i)
			options[i]= new ImageRequestOptions(image.getLong("imageId"), image.getInt("ram").toInteger(), image.getInt("cores"), image.getInt("instances"))
		}
		def userCluster= Cluster.get(cluster.get("clusterId"))
		return deploymentService.deployMultipleOptions(userCluster, user, cluster.getInt("execTime")*60000,options)
	}

	def getClusterList(String login,String apiKey){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login);
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		return user.userClusters
	}
	
	def getActiveDeployments(String login,String apiKey){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login);
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		Collection deps
		for(dep in user.deployments){
			if (dep.status.equals(DeploymentStateEnum.ACTIVE))
			deps.add(dep)
		}
		return deps
	}
	
	def getDeploymentInfo(String login,String apiKey,String depId){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login);
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		return Deployment.get(depId).cluster
	}
}
