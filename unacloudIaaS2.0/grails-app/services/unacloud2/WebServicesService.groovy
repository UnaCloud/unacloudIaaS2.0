package unacloud2

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import wsEntities.WebServiceException


class WebServicesService {
	DeploymentService deploymentService
    
	def startCluster(String login,String apiKey,JSONObject cluster) {
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login)
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		
		Cluster userCluster= Cluster.findByName(cluster.get("cluster_name"))
		def instance=cluster.getJSONArray("number_instances")
		def iRAM=cluster.getJSONArray("ram_values")
		def iCores=cluster.getJSONArray("cores")
		def time=cluster.getJSONArray("exec_time")
		instance=(instance.length()==1)?instance.getInt(0):instance.toArray()
		iCores=(iCores.length()==1)?iCores.getInt(0):iCores.toArray()
		iRAM=(iRAM.length()==1)?iRAM.getInt(0):iRAM.toArray()
		time=(time.length()==1)?time.getInt(0):time.toArray()
		deploymentService.deploy(userCluster, user, instance,  iRAM, iCores, time)
	}
	
	def getClusterList(String login,String apiKey){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login);
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		return user.userClusters
	}
}
