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
	
	def stopDeployment(String login,String apiKey,String depId){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login)
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		Deployment deployment= Deployment.get(depId)
		def deps=user.deployments
		def belongsToUser= false
		for (dep in deps){
			if(dep.equals(deployment)){
				belongsToUser=true
			}
		}
		if(!belongsToUser)return new WebServiceException("Cannot stop that deployment because it doesn´t belong to user")
		for(image in deployment.cluster.images){
			for(vm in image.virtualMachines){
				deploymentService.stopVirtualMachineExecution(vm)
			}
		}
		deploymentService.stopDeployments(user)
		return deployment
	}
	
	def stopVirtualMachine(String login,String apiKey,String machineId){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login)
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		VirtualMachineExecution vme= VirtualMachineExecution.get(machineId)
		def deps=user.deployments
		def belongsToUser= false
		for (dep in deps){
			if(dep.isActive()){
			for(image in dep.cluster.images){
				for (vm in image.virtualMachines){
					if(vm.equals(vme)){
						belongsToUser=true
						break
					}
				}
			}
			}
		}
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Cannot stop that machine because it doesn´t belong to user")
		def resp =deploymentService.stopVirtualMachineExecution(vme)	
		deploymentService.stopDeployments(user)
		return resp
	}

	def startHeterogeneousCluster(String login,String apiKey,JSONObject cluster) {
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
		return deploymentService.deployHeterogeneous(userCluster, user, cluster.getInt("execTime")*60000,options)
	}

	def getClusterList(String login,String apiKey){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login);
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		def clusterList= new JSONArray()
		
		for(cluster in user.userClusters){
			def clusterProperties= new JSONObject()
			clusterProperties.put("cluster_id",cluster.id)
			clusterProperties.put("name",cluster.name)	
			def images= new JSONArray()
			for(image in cluster.images){
				JSONObject imageProperties= new JSONObject()
				imageProperties.put("image_id", image.id)
				imageProperties.put("name", image.name)
				images.put(imageProperties)
			}
			clusterProperties.put("images",images)
			clusterList.put(clusterProperties)
		}
		return clusterList
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
		JSONArray resp= new JSONArray()
		for(dep in deps){
			JSONObject depProps= new JSONObject()
			depProps.put("deployment_id", dep.id)
			depProps.put("cluster_name", dep.cluster.cluster.name)
			depProps.put("cluster_id", dep.cluster.cluster.id)
			resp.put(depProps)
		}
		return resp
	}
	
	def getDeploymentInfo(String login,String apiKey,String depId){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login);
		if(user==null||user.apiKey==null)return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey))return new WebServiceException("Invalid Key")
		
		
		def vms= new JSONArray()
		def dep= Deployment.get(depId)
		if(!dep.isActive())return new WebServiceException("This deployment is not active")
		for (image in dep.cluster.images){
			for(vm in image.virtualMachines){
				def data= new JSONObject()
				data.put("belongs_to_image",image.image.name)
				data.put("status",vm.status.toString())
				data.put("stop_time", vm.stopTime.getTime())
				data.put("ip",vm.ip.ip)
				data.put("message",vm.message)
				data.put("hostname",vm.name)
				data.put("id", vm.id)
				vms.put( data)
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
	
	def addInstances(String login,String apiKey,String imageId,int instances,long time){
		if(login==null||apiKey==null)return new WebServiceException("invalid request")
		User user= User.findByUsername(login);
		if(user.userType.equals("User")) return new WebServiceException("You're not administrator")
		if(user==null||user.apiKey==null) return new WebServiceException("Invalid User")
		if(!apiKey.equals(user.apiKey)) return new WebServiceException("Invalid Key")
		DeployedImage image= DeployedImage.get(imageId)
		if(image==null)return new WebServiceException("Image not found")
		boolean belongsToUser=false
		for(dep in user.deployments){
			for(i in dep.cluster.images){
				if(image.equals(i)){
					belongsToUser=true
					break
				}
			}
		}
		if(!belongsToUser)return new WebServiceException("This image wasn't deployed by the given user")
		return deploymentService.addInstances(image, instances, time.toLong()*60*1000)
	}
}
