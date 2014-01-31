package unacloud2

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import back.pmallocators.AllocatorEnum;
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
		def deps=user.deployments
		def belongsToUser= false
		for (dep in deps){
			if(dep.cluster.equals(cluster)){
				belongsToUser=true
			}
		}
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Cannot stop that cluster because it doesn´t belong to user")
		for(image in cluster.images){
			for(vm in image.virtualMachines){
				deploymentService.stopVirtualMachineExecution(vm)	
			}
		}
		deploymentService.stopDeployments(user)
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
		def deps= new ArrayList()
		for(dep in user.deployments){
			if (dep.status.equals(DeploymentStateEnum.ACTIVE))
			deps.add(dep)
		}
		if (deps.isEmpty())return new WebServiceException("There's no active deployments for this user")
		return deps
	}
	
	def getDeploymentInfo(String login,String apiKey,String depId){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login);
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		def vms= new JSONObject()
		def dep= Deployment.get(depId)
		if(!dep.isActive())return new WebServiceException("This deployment is not active")
		for (image in dep.cluster.images){
			for(vm in image.virtualMachines){
				def data= new JSONObject()
				data.put("belongs_to_image",image.image.name)
				data.put("status",vm.status.toString())
				data.put("time_left", vm.remainingTime())
				data.put("ip",vm.ip.ip)
				data.put("hostname",vm.name)
				vms.put("vm_data", data)
			}
		}
		return vms
	}
	
	def changeAllocationPolicy(String login,String apiKey,String allocationPolicy){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login);
		if(user.userType.equals("User")) return new WebServiceException("You're not administrator")
		if(user==null||user.apiKey==null) return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey)) return new WebServiceException("Invalid Key")
		def alloc
		try{
			alloc=AllocatorEnum.valueOf(allocationPolicy)
		}catch(Exception e){
			return e
		}
		
		if(alloc==null) throw new WebServiceException("Allocator not found")
		def variable=ServerVariable.findByName("VM_ALLOCATOR_NAME")
		variable.putAt("variable", alloc.toString())
		return "Success"
		
	}
}
