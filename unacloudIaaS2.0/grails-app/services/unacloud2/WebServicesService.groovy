package unacloud2

import org.codehaus.groovy.grails.web.json.JSONObject


class WebServicesService {
	DeploymentService deploymentService
    
	def startCluster(JSONObject cluster) {
		def instance=cluster.getJSONArray("number_instances")
		def iRAM=cluster.getJSONArray("ram_values")
		def iCores=cluster.getJSONArray("cores")
		def time=cluster.getJSONArray("exec_times")
		instance=(instance.length()==1)?instance.getInt(0):instance.toArray()
		iCores=(iCores.length()==1)?iCores.getInt(0):iCores.toArray()
		iRAM=(iRAM.length()==1)?iRAM.getInt(0):iRAM.toArray()
		time=(time.length()==1)?time.getInt(0):time.toArray()
		deploymentService.deploy(Cluster.get(cluster.get("cluster_id")), User.get(cluster.get("API_key")), instance,  iRAM, iCores, time)
	}
	
	def getClusterList(JSONObject API_key){
		JSONObject clusters= new JSONObject()
		User.get(API_key.get("API_key")).userClusters.each {
			clusters.put(it.name,it.images)
		}
		return clusters
		
	}
}
