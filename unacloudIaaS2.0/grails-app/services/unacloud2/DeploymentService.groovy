package unacloud2

import unacloudEnums.VirtualMachineExecutionStateEnum;
import back.allocators.PhysicalMachineAllocatorService;
import back.deployers.DeployerService;


class DeploymentService {
   
	PhysicalMachineAllocatorService physicalMachineAllocatorService
	DeployerService deployerService
	
   def deployImage(VirtualMachineImage image, User user){
		
		long stopTimeMillis= new Date().getTime()
		def stopTime= new Date(stopTimeMillis +(60*60*1000))
		def virtualMachine = new VirtualMachineExecution(message: "Initializing", name: image.name+"-"+1 ,ram:512 , cores:1 ,disk: image.fixedDiskSize , status: VirtualMachineExecutionStateEnum.COPYING, startTime: new Date(), stopTime: stopTime )
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
		if(depImage.virtualMachines.size()>0){
		physicalMachineAllocatorService.allocatePhysicalMachinesRandomly(cluster)
		}
		runAsync{
			deployerService.deploy(dep)
		}
		user.save(failOnError: true)
	}
   
   	def deploy(Cluster cluster, User user, instance,  iRAM, iCores, time){
	   
	   DeployedCluster depCluster= new DeployedCluster(cluster: cluster)
	   depCluster.images=[]
	   println iRAM
	   int images= cluster.images.size()
	   println images
	   boolean onlyOneImage= (images==1)
	   cluster.images.eachWithIndex(){ image,i->
		   def depImage= new DeployedImage(image:image)
		   depImage.virtualMachines= []
		   for(int j=0;j<((Integer.parseInt(instance.getAt(i))));j++){
			   println "NUMERO DE INSTANCIAS:===============>"+instance
			   long stopTimeMillis= new Date().getTime()
			   def stopTime= new Date(stopTimeMillis +Integer.parseInt(time))
			   def iName=image.name
			   def virtualMachine = new VirtualMachineExecution(message: "Initializing", name: iName +"-"+j ,ram: (onlyOneImage)?iRAM:iRAM.getAt(i), cores:iCores.getAt(i),disk:0,status: VirtualMachineExecutionStateEnum.DEPLOYING,startTime: new Date(),stopTime: stopTime )
			   depImage.virtualMachines.add(virtualMachine)
			   virtualMachine.save(failOnError: true)
			   depImage.save(failOnError: true)
		   }
		   depCluster.images.add(depImage)
	   }
	   depCluster.save(failOnError: true)
	   physicalMachineAllocatorService.allocatePhysicalMachinesRandomly(depCluster)
	   long stopTimeMillis= new Date().getTime()
	   def stopTime= new Date(stopTimeMillis +Integer.parseInt(time))
	   Deployment dep= new Deployment(cluster: depCluster,startTime: new Date(),stopTime: stopTime,status: DeploymentStateEnum.ACTIVE)
	   dep.save(failOnError: true)
	   if(user.deployments==null)
		   user.deployments=[]
	   user.deployments.add(dep)
	   user.save(failOnError: true)
	   println "ahora si voy a llamar"
	   runAsync{
		   deployerService.deploy(dep)   
	   }
	   
   }
	   
	def stopVirtualMachineExecution(VirtualMachineExecution vm){
		vm.stopTime=new Date()
		vm.ip.used=false
		vm.status= VirtualMachineExecutionStateEnum.FINISHED
		vm.save()
		
		deployerService.stopVirtualMachine(vm)
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
					def virtualMachine = new VirtualMachineExecution(message: "Deploying",name: iName+"-"+i ,ram: iRAM, cores:iCores,disk:0,status: VirtualMachineExecutionStateEnum.DEPLOYING, startTime: new Date(), stopTime: stopTime )
					physicalMachineAllocatorService.allocatePhysicalMachine(virtualMachine)
					virtualMachine.save(failOnError: true)
					depImage.virtualMachines.add(virtualMachine)
		}
		
		depImage.save(failOnError: true)	
	}
}
