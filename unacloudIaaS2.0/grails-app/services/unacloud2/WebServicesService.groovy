package unacloud2

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject


class WebServicesService {
	DeploymentService deploymentService
    
	def startCluster(JSONObject cluster) {
		User user= User.findByUsername(cluster.get("login"))
		if(user.apiKey.equals(cluster.getString("API_key")))
		{
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
	}
	
	def getClusterList(JSONObject API_key){
		User user= User.findByUsername(API_key.get("login"));
		if(API_key.get("API_key").equals(user.apiKey)){
			return user.userClusters
		}
		return null
	}
}
