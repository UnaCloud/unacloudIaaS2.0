package unacloud2

class Deployment {
	
	DeployedCluster cluster
	Date startTime
	Date stopTime
	DeploymentStateEnum status
	
	static constraints = {
		
    }
	
	def getTotalActiveVMs(){
		def totalVMs =0
		cluster.images.each {
			it.virtualMachines.each {
				if(it.status!= VirtualMachineExecutionStateEnum.FINISHED)
				totalVMs++
			}
		}
		return totalVMs
	}
	
	def isActive(){
		for(image in cluster.images) {
			for(vm in image.virtualMachines){
				if(!(vm.status ==VirtualMachineExecutionStateEnum.FINISHED)){
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
				if(vm.status ==VirtualMachineExecutionStateEnum.COPYING){
					vm.status = VirtualMachineExecutionStateEnum.CONFIGURING
					vm.save()
				}
			}
		}
	}
	
	def configureVMs(){
		for(image in cluster.images) {
			for(vm in image.virtualMachines){
				if(vm.status ==VirtualMachineExecutionStateEnum.CONFIGURING){
					vm.status = VirtualMachineExecutionStateEnum.DEPLOYING
					vm.save()
				}
			}
		}
	}
	
	def deployVMs(){
		for(image in cluster.images) {
			for(vm in image.virtualMachines){
				if(vm.status == VirtualMachineExecutionStateEnum.DEPLOYING){
					vm.status = VirtualMachineExecutionStateEnum.DEPLOYED
					vm.save()
				}
			}
		}
	}
}
