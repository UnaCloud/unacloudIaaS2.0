package unacloud2

import grails.util.Environment;
import unacloudEnums.VirtualMachineExecutionStateEnum;
import webutils.ImageRequestOptions;
import back.deployers.DeployerService;
import back.deploymentBuilder.DeploymentProcessorService;


class DeploymentService {
	
	//-----------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------
	
	/**
	 * Representation of deployment builder service
	 */
	DeploymentProcessorService deploymentProcessorService
	
	/**
	 * Representation of deployment message sender service 
	 */
	DeployerService deployerService
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------
	
	/**
	 * Deploys a new cluster which virtual machines could be
	 * heterogeneous for the same image  
	 * @param cluster cluster to be deployed
	 * @param user owner of the deployment
	 * @param time execution time in minutes
	 * @param options group of deployment properties for each image
	 * @throws Exception if any of the deployment processes fails
	 * @return dep created deployment entity
	 */
	
	def deployHeterogeneous(Cluster cluster, User user, long time, ImageRequestOptions[] options) throws Exception{
		println "Deploying"
		/*
		 * Creates a new empty deployed cluster
		 */
		DeployedCluster depCluster= new DeployedCluster(cluster: cluster)
		depCluster.images=[]
		options.eachWithIndex(){ image,i->
			/*
			 * Iterates over each option in parameters and looks for the 
			 * matching image, creating the respective deployed image   
			 */
			def depImage= new DeployedImage(image:VirtualMachineImage.get(image.imageId))
			depImage.virtualMachines= []
			int option
			/*
			 * then creates the new nodes according to user request 
			 */
			for(int j=0;j<image.instances;j++){
				long stopTimeMillis= new Date().getTime()
				def stopTime= new Date(stopTimeMillis +time)
				def iName=depImage.image.name
				def virtualMachine = new VirtualMachineExecution(message: "Initializing", name: image.hostname, ram: image.ram, cores: image.cores,disk:0,status: VirtualMachineExecutionStateEnum.DEPLOYING,startTime: new Date(),stopTime: stopTime )
				depImage.virtualMachines.add(virtualMachine)
				virtualMachine.save(failOnError: true)
				depImage.save(failOnError: true)
			}
			/*
			 * Fills the cluster with the deployed images
			 */
			depCluster.images.add(depImage)
		}
		depCluster.save(failOnError: true)
		/*
		 * Makes allocation and user restriction validations for each image
		 */
		for(image in depCluster.images)
		deploymentProcessorService.doDeployment(image,user,false)
		/*
		 * Creates deployment entity and links it to the user
		 */
		long stopTimeMillis= new Date().getTime()
		def stopTime= new Date(stopTimeMillis +time)
		Deployment dep= new Deployment(cluster: depCluster,startTime: new Date(),stopTime: stopTime,status: DeploymentStateEnum.ACTIVE)
		dep.save(failOnError: true)
		if(user.deployments==null)
			user.deployments=[]
		user.deployments.add(dep)
		user.save(failOnError: true)
		/*
		 * Finally it sends the deployment message to the agents
		 */
		if(!Environment.isDevelopmentMode())
		runAsync{ deployerService.deploy(dep) }
		return dep
	}
	
	/**
	 * Deploys a new cluster which virtual machines could be
	 * heterogeneous for the same image  
	 * @param cluster cluster to be deployed
	 * @param user owner of the deployment
	 * @param time execution time in minutes
	 * @param options group of deployment properties for each image
	 * @param highAvailability indicates if the images will be executed in a
	 * dedicated server
	 * @throws Exception if any of the deployment processes fails
	 * @return dep created deployment entity
	 */
	
	def deploy(Cluster cluster, User user, long time, ImageRequestOptions[] options, highAvailability) throws Exception{
		/*
		 * Creates a new empty deployed cluster
		 */
		
		println "Deploying"
		DeployedCluster depCluster= new DeployedCluster(cluster: cluster)
		depCluster.images=[]
		/*
		 * Iterates over each option in parameters and looks for the
		 * matching image, creating the respective deployed image
		 */
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
			depImage.setHighAvaliavility(highAvailability[option])
			/*
			 * then creates the new nodes according to user request
			 */
			for(int j=0;j<options[option].instances;j++){
				long stopTimeMillis= new Date().getTime()
				println stopTimeMillis
				println "millis:"+(stopTimeMillis+time)
				def stopTime= new Date(stopTimeMillis +time)
				println "Stop date"+stopTime
				def iName=image.name
				def virtualMachine = new VirtualMachineExecution(message: "Initializing", name: options[option].hostname, ram: options[option].ram, cores: options[option].cores,disk:0,status: VirtualMachineExecutionStateEnum.DEPLOYING,startTime: new Date(),stopTime: stopTime )
				depImage.virtualMachines.add(virtualMachine)
				virtualMachine.save(failOnError: true)
				depImage.save(failOnError: true)
			}
			/*
			 * Fills the cluster with the deployed images
			 */
			depCluster.images.add(depImage)
		}
		depCluster.save(failOnError: true)
		/*
		 * Makes allocation and user restriction validations for each image
		 */
		for (image in depCluster.images)
		deploymentProcessorService.doDeployment(image,user,false)
		/*
		 * Creates deployment entity and links it to the user
		 */
		long stopTimeMillis= new Date().getTime()
		def stopTime= new Date(stopTimeMillis +time)
		Deployment dep= new Deployment(cluster: depCluster, startTime: new Date(),stopTime: stopTime,status: DeploymentStateEnum.ACTIVE)
		dep.save(failOnError: true)
		if(user.deployments==null)
			user.deployments=[]
		user.deployments.add(dep)
		user.save(failOnError: true)
		/*
		 * Finally it sends the deployment message to the agents
		 */
		if(!Environment.isDevelopmentMode())
		runAsync{ deployerService.deploy(dep) }
		
		return dep
	}
	
	/**
	 * Stops an execution of a virtual machine
	 * @param vm Virtual machine to be stopped
	 * @return vm virtual machine with the new state
	 */
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
		return vm
	}
	
	/**
	 * Refreshes all user deployment states. 
	 * @param user deployment owner
	 */
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
	
	/**
	 * Adds new instances to the selected deployed image
	 * @param depImage deployed image associated with new instances
	 * @param user owner of the deployed image
	 * @param instance number of new instances
	 * @param time execution time of the new instances
	 * @return depImage deployed image with the new changes
	 * @throws Exception if any of the deployment processes fails
	 */
	def addInstances(DeployedImage depImage,User user, int instance, long time) throws Exception{
		
		/*
		 * Gets the new virtual machine properties 
		 */
		def iName=depImage.getDeployedHostname()
		def iRAM=depImage.getDeployedRAM()
		def iCores= depImage.getDeployedCores()
		def index=depImage.virtualMachines.size()
		
		/*
		 * Creates new nodes
		 */
		
		for(int i=index;i<instance+index;i++){
			long stopTimeMillis= new Date().getTime()
			def stopTime= new Date(stopTimeMillis +time)
			def virtualMachine = new VirtualMachineExecution(message: "Adding instance",name: iName,ram: iRAM, cores:iCores,disk:0,status: VirtualMachineExecutionStateEnum.DEPLOYING, startTime: new Date(), stopTime: stopTime )
			//ipAllocatorService.allocatePhysicalMachine(virtualMachine)
			virtualMachine.save(failOnError: true)
			depImage.virtualMachines.add(virtualMachine)
		}
		
		/*
		 * Makes allocation and user restriction validations for new nodes
		 */
		
		deploymentProcessorService.doDeployment(depImage,user,true)
		
		/*
		 * Finally it sends the deployment message to the agents
		 */
		
		runAsync{ deployerService.deployNewInstances(depImage) }
		depImage.save(failOnError: true)
		return depImage
	}
}
