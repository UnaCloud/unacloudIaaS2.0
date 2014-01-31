package unacloud2

import grails.util.Environment;
import unacloudEnums.VirtualMachineExecutionStateEnum;
import webutils.ImageRequestOptions;
import back.deployers.DeployerService;
import back.deploymentBuilder.DeploymentProcessorService;


class DeploymentService {
	DeploymentProcessorService deploymentProcessorService 
	DeployerService deployerService

	def deployImage(VirtualMachineImage image, User user){

		long stopTimeMillis= new Date().getTime()
		def stopTime= new Date(stopTimeMillis +(60*60*1000))
		def virtualMachine = new VirtualMachineExecution(message: "Initializing", name: image.name+"-"+1 ,ram:512 , cores:1 ,disk: image.fixedDiskSize , status: VirtualMachineExecutionStateEnum.DEPLOYING, startTime: new Date(), stopTime: stopTime )
		virtualMachine.save()
		DeployedImage depImage= new DeployedImage(image:image)
		depImage.addToVirtualMachines(virtualMachine)
		depImage.save()
		DeployedCluster cluster= new DeployedCluster(cluster: null)
		cluster.addToImages(depImage)
		Deployment dep= new Deployment(cluster: cluster, startTime: new Date(), stopTime: stopTime, totalVMs: 1, status: DeploymentStateEnum.ACTIVE)
		cluster.save()
		dep.save()
		if(user.deployments==null)
			user.deployments=[]
		user.addToDeployments(dep)
		println "allocando"
		if(depImage.virtualMachines.size()>0 && !Environment.isDevelopmentMode()){
			deploymentProcessorService.doDeployment(cluster,false)
			runAsync{ deployerService.deploy(dep) }
		}
		user.save(failOnError: true)
	}

	def deployMultipleOptions(Cluster cluster, User user, long time, ImageRequestOptions[] options){
		println "Deploying"
		DeployedCluster depCluster= new DeployedCluster(cluster: cluster)
		depCluster.images=[]
		options.eachWithIndex(){ image,i->
			def depImage= new DeployedImage(image:VirtualMachineImage.get(image.imageId))
			depImage.virtualMachines= []
			int option

			for(int j=0;j<image.instances;j++){
				long stopTimeMillis= new Date().getTime()
				def stopTime= new Date(stopTimeMillis +time)
				def iName=depImage.image.name
				def virtualMachine = new VirtualMachineExecution(message: "Initializing", name: iName +"-"+j, ram: image.ram, cores: image.cores,disk:0,status: VirtualMachineExecutionStateEnum.DEPLOYING,startTime: new Date(),stopTime: stopTime )
				depImage.virtualMachines.add(virtualMachine)
				virtualMachine.save(failOnError: true)
				depImage.save(failOnError: true)
			}
			depCluster.images.add(depImage)
		}
		depCluster.save(failOnError: true)
		deploymentProcessorService.doDeployment(depCluster,false)
		//ipAllocatorService.allocatePhysicalMachinesRandomly(depCluster)
		long stopTimeMillis= new Date().getTime()
		def stopTime= new Date(stopTimeMillis +time)
		Deployment dep= new Deployment(cluster: depCluster,startTime: new Date(),stopTime: stopTime,status: DeploymentStateEnum.ACTIVE)
		dep.save(failOnError: true)
		if(user.deployments==null)
			user.deployments=[]
		user.deployments.add(dep)
		user.save(failOnError: true)
		if(!Environment.isDevelopmentMode())
		runAsync{ deployerService.deploy(dep) }
		return dep.id
	}

	def deploy(Cluster cluster, User user, long time, ImageRequestOptions[] options){
		println "Deploying"
		DeployedCluster depCluster= new DeployedCluster(cluster: cluster)
		depCluster.images=[]
		cluster.images.eachWithIndex(){ image,i->
			def depImage= new DeployedImage(image:image)
			depImage.virtualMachines= []
			int option
			for(int j=0;j<options.length;j++){
				if (options[j].imageId==image.id){
					option=j
					break
				}
			}
			if(option==null){
				return
			}
			for(int j=0;j<options[option].instances;j++){
				long stopTimeMillis= new Date().getTime()
				println stopTimeMillis
				println "millis:"+(stopTimeMillis+time)
				def stopTime= new Date(stopTimeMillis +time)
				println "Stop date"+stopTime
				def iName=image.name
				def virtualMachine = new VirtualMachineExecution(message: "Initializing", name: iName +"-"+j, ram: options[option].ram, cores: options[option].cores,disk:0,status: VirtualMachineExecutionStateEnum.DEPLOYING,startTime: new Date(),stopTime: stopTime )
				depImage.virtualMachines.add(virtualMachine)
				virtualMachine.save(failOnError: true)
				depImage.save(failOnError: true)
			}
			depCluster.images.add(depImage)
		}
		depCluster.save(failOnError: true)
		deploymentProcessorService.doDeployment(depCluster,false)
		//ipAllocatorService.allocatePhysicalMachines(depCluster)
		long stopTimeMillis= new Date().getTime()
		def stopTime= new Date(stopTimeMillis +time)
		Deployment dep= new Deployment(cluster: depCluster,startTime: new Date(),stopTime: stopTime,status: DeploymentStateEnum.ACTIVE)
		dep.save(failOnError: true)
		if(user.deployments==null)
			user.deployments=[]
		user.deployments.add(dep)
		user.save(failOnError: true)
		if(!Environment.isDevelopmentMode())
		runAsync{ deployerService.deploy(dep) }
		
		return dep
	}
	def deployCCSACluster(Cluster cluster, User user, long time, ImageRequestOptions[] options){
		println "Deploying"
		DeployedCluster depCluster= new DeployedCluster(cluster: cluster)
		depCluster.images=[]
		cluster.images.eachWithIndex(){ image,i->
			def depImage= new DeployedImage(image:image)
			depImage.virtualMachines= []
			int option
			for(int j=0;j<options.length;j++){
				if (options[j].imageId==image.id){
					option=j
					break
				}
				
			}
			
			if(option==null){
				return
			}
			for(int j=0;j<options[option].instances;j++){
				long stopTimeMillis= new Date().getTime()
				def stopTime= new Date(stopTimeMillis +time)
				def iName=image.name
				def virtualMachine = new VirtualMachineExecution(message: "Initializing", name: iName +"-"+j, ram: options[option].ram, cores: options[option].cores,disk:0,status: VirtualMachineExecutionStateEnum.DEPLOYING,startTime: new Date(),stopTime: stopTime )
				depImage.virtualMachines.add(virtualMachine)
				virtualMachine.save(failOnError: true)
				depImage.save(failOnError: true)
			}
			depCluster.images.add(depImage)
		}
		depCluster.save(failOnError: true)
		deploymentProcessorService.doDeployment(depCluster,false)
		//ipAllocatorService.allocatePhysicalMachines(depCluster)
		long stopTimeMillis= new Date().getTime()
		def stopTime= new Date(stopTimeMillis +time)
		Deployment dep= new Deployment(cluster: depCluster,startTime: new Date(),stopTime: stopTime,status: DeploymentStateEnum.ACTIVE)
		dep.save(failOnError: true)
		if(user.deployments==null)
			user.deployments=[]
		user.deployments.add(dep)
		user.save(failOnError: true)
		runAsync{ deployerService.deploy(dep) }
		return dep.id
	}
	def stopVirtualMachineExecution(VirtualMachineExecution vm){
		if(vm.status==VirtualMachineExecutionStateEnum.DEPLOYED||vm.status==VirtualMachineExecutionStateEnum.FAILED){
		vm.stopTime=new Date()
		vm.ip.used=false
		vm.ip.save()
		vm.status= VirtualMachineExecutionStateEnum.FINISHED
		vm.save()
		if(!Environment.isDevelopmentMode()||vm.status==VirtualMachineExecutionStateEnum.FAILED)
		deployerService.stopVirtualMachine(vm)
		}
	}

	def stopDeployments(User user){
		def dep= user.getActiveDeployments()
		dep.each {
			if(!it.isActive()){
				it.status=DeploymentStateEnum.FINISHED
				it.stopTime=new Date()
				it.save()
			}
		}
	}

	def addInstances(DeployedImage depImage, int instance, time){

		def iName=depImage.image.name
		def iRAM=depImage.getDeployedRAM()
		def iCores= depImage.getDeployedCores()
		def index=depImage.virtualMachines.size()
		for(int i=index;i<instance+index;i++){
			long stopTimeMillis= new Date().getTime()
			def stopTime= new Date(stopTimeMillis +Integer.parseInt(time))
			def virtualMachine = new VirtualMachineExecution(message: "Adding instance",name: iName+"-"+i ,ram: iRAM, cores:iCores,disk:0,status: VirtualMachineExecutionStateEnum.DEPLOYING, startTime: new Date(), stopTime: stopTime )
			//ipAllocatorService.allocatePhysicalMachine(virtualMachine)
			virtualMachine.save(failOnError: true)
			depImage.virtualMachines.add(virtualMachine)
		}
		DeployedCluster cluster= new DeployedCluster(images: depImage)
		deploymentProcessorService.doDeployment(cluster,true)
		runAsync{ deployerService.deployNewInstances(depImage) }
		depImage.save(failOnError: true)
	}
}
