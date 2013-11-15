package unacloud2

class Deployment {
	
	
	DeployedCluster cluster
	Date startTime
	Date stopTime
	int status
	
	int totalVMs
	
	static DEPLOYING=1
	static FINISHED=2
	
	static constraints = {
		
    }
	
	def getTotalActiveVMs(){
		def totalVMs =0
		cluster.images.each {
			it.virtualMachines.each {
				if(it.status!= VirtualMachine.FINISHED)
				totalVMs++
			}
		}
		return totalVMs
	}
	
	def isActive(){
		for(image in cluster.images) {
			for(vm in image.virtualMachines){
				if(!(vm.status ==VirtualMachine.FINISHED)){
					return true
				}
			}
		}
		return false
	}
	
	
	def	deploy(){
		runAsync{
			copyVMs()
			sleep(10000)
			configureVMs()
			sleep(10000)
			deployVMs()	
		}
	}
	
	
	def copyVMs(){
		for(image in cluster.images) {
			for(vm in image.virtualMachines){
				if(vm.status ==VirtualMachine.COPYING){
					vm.status = VirtualMachine.CONFIGURING
					vm.save()
				}
			}
		}
	}
	
	def configureVMs(){
		for(image in cluster.images) {
			for(vm in image.virtualMachines){
				if(vm.status ==VirtualMachine.CONFIGURING){
					vm.status = VirtualMachine.DEPLOYING
					vm.save()
				}
			}
		}
	}
	
	def deployVMs(){
		for(image in cluster.images) {
			for(vm in image.virtualMachines){
				if(vm.status ==VirtualMachine.DEPLOYING){
					vm.status = VirtualMachine.DEPLOYED
					vm.save()
				}
			}
		}
	}
}
