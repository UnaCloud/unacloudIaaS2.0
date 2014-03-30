package unacloud2

import unacloudEnums.VirtualMachineExecutionStateEnum;

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
	
	def updateState(){
		for(image in cluster.images) {
			for(vm in image.virtualMachines){
				if(!(vm.status ==VirtualMachineExecutionStateEnum.FINISHED)){
				if(vm.stopTime.compareTo(new Date())<0){
					vm.status= VirtualMachineExecutionStateEnum.FINISHED
					if (vm.ip != null) vm.ip.used= false
				}
				else if((((new Date().getTime()-vm.startTime.getTime())/60000))>30 && vm.status==VirtualMachineExecutionStateEnum.DEPLOYING){
					vm.status=VirtualMachineExecutionStateEnum.FAILED
					vm.message='Request timeout'
				}
				}			}
		}
	}
	
	def isActive(){
		if (status==DeploymentStateEnum.ACTIVE){
		updateState()
		for(image in cluster.images) {
			for(vm in image.virtualMachines){
				if(!(vm.status ==VirtualMachineExecutionStateEnum.FINISHED))
					return true
			}
		}
		}
		return false
	}
	
	
}
