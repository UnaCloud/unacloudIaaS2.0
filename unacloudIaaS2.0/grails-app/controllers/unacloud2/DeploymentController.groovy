package unacloud2

import java.util.regex.Pattern.Start;

class DeploymentController {
	
	def beforeInterceptor = {
		if(!session.user){
			
			flash.message="You must log in first"
			redirect(uri:"/login", absolute:true)
			return false
		}
		session.user.refresh()
	}
	
    def index() { 
		[deployments: session.user.getActiveDeployments()]
	}
	def addInstancesOptions(){
		[id:params.id]
	}
	
	def deployImage(){
		
		def image= VirtualMachineImage.get(params.id)
		if (!image.isDeployed()){
		long stopTimeMillis= new Date().getTime()
		def stopTime= new Date(stopTimeMillis +(60*60*1000))
		IP ip= new IP(ip: "10.0.0.1",used: true)
		def virtualMachine = new VirtualMachine(name: image.name+"-"+1 ,ram:512 , cores:1 ,disk: image.fixedDiskSize ,ip: ip , status: VirtualMachine.DEPLOYING, startTime: new Date(), stopTime: stopTime )
		ip.save()
		DeployedImage depImage= new DeployedImage(image:image)
		depImage.virtualMachines=[]
		depImage.virtualMachines.add(virtualMachine)
		virtualMachine.save()
		DeployedCluster cluster= new DeployedCluster(cluster: null)
		cluster.images=[]
		cluster.images.add(depImage)
		depImage.save()
		Deployment dep= new Deployment(cluster: cluster, startTime: new Date(), stopTime: stopTime, totalVMs: 1, status: Deployment.DEPLOYING)
		cluster.save()
		dep.save()
		def user= User.get(session.user.id)
		if(user.deployments==null)
			user.deployments=[]
		user.deployments.add(dep)
		user.save(failOnError: true)
		redirect(action: "index")
		}
		else
		{
			flash.message= "Image already deployed"
			redirect(controller:"virtualMachineImage",action:"index")
		}
	}
	
	def deploy(){
		
		Cluster cluster= Cluster.get(params.id)
		def deployments= Deployment.list()
		if(!cluster.isDeployed()){
		DeployedCluster depCluster= new DeployedCluster(cluster: cluster)
		depCluster.images=[]
		cluster.images.eachWithIndex(){ image,i->
			def depImage= new DeployedImage(image:image)
			depImage.virtualMachines= []
			def instance= Integer.parseInt(params.instances.getAt(i))
			def iRAM= params.RAM.getAt(i)
			def iCores= params.cores.getAt(i)
			def iName= image.name
			for(int j=0;j<instance;j++){
					def iIP= new IP(ip: "10.0.1."+j)
					iIP.save(failOnError: true)
					long stopTimeMillis= new Date().getTime()
					def stopTime= new Date(stopTimeMillis +Integer.parseInt(params.time))
					
					def virtualMachine = new VirtualMachine(name: iName+"-"+j ,ram: iRAM, cores:iCores,disk:0,ip: iIP,status: VirtualMachine.DEPLOYING,startTime: new Date(),stopTime: stopTime )
					virtualMachine.save(failOnError: true)
					depImage.virtualMachines.add(virtualMachine)
			}
			depImage.save(failOnError: true)
			depCluster.images.add(depImage)
			
			//crear vms
		}
		depCluster.save(failOnError: true)
		long stopTimeMillis= new Date().getTime()
		def stopTime= new Date(stopTimeMillis +Integer.parseInt(params.time))
		def totalVMs =0
		params.instances.each {
			totalVMs+=Integer.parseInt(it)
		}
		Deployment dep= new Deployment(totalVMs:totalVMs,cluster: depCluster,startTime: new Date(),stopTime: stopTime,status: Deployment.DEPLOYING)
		dep.save(failOnError: true)
		def user= User.get(session.user.id)
		if(user.deployments==null)
			user.deployments=[]
		user.deployments.add(dep)
		user.save(failOnError: true)
		dep.deploy();
		redirect(controller:"deployment")
		}
		else{
			flash.message="Cluster already deployed"
			redirect( controller: "cluster",action: "index" )
		}
	
	}
	
	def history(){
		
		[deployments: session.user.deployments]
	}
	
	def stop(){
		params.each {
			if (it.key.contains("hostname")){
				if (it.value.contains("on")){
					VirtualMachine vm = VirtualMachine.get((it.key - "hostname") as Integer)
					vm.stopTime=new Date()
					vm.status= VirtualMachine.FINISHED
					vm.save()
				}
			}
		}
		
		def dep= session.user.getActiveDeployments()
		dep.each {
			if(!it.isActive()){
			it.status=Deployment.FINISHED
			it.stopTime=new Date()
			it.save()
			}
		}
		redirect(action:"index")
	}
	
	def addInstances(){
		def depImage=DeployedImage.get(params.id)
		
		def instance=params.instances.toInteger()
		def iName=depImage.image.name
		def iRAM=depImage.getDeployedRAM()
		def iCores= depImage.getDeployedCores()
		def index=depImage.virtualMachines.size()
		for(int i=index;i<instance+index;i++){
					def iIP= new IP(ip: "10.0.1."+i)
					iIP.save(failOnError: true)
					long stopTimeMillis= new Date().getTime()
					def stopTime= new Date(stopTimeMillis +Integer.parseInt(params.time))
					
					def virtualMachine = new VirtualMachine(name: iName+"-"+i ,ram: iRAM, cores:iCores,disk:0,ip: iIP,status: VirtualMachine.DEPLOYING,startTime: new Date(),stopTime: stopTime )
					virtualMachine.save(failOnError: true)
					depImage.virtualMachines.add(virtualMachine)
			}
			depImage.save(failOnError: true)
		redirect(action: "index")
	}
}
