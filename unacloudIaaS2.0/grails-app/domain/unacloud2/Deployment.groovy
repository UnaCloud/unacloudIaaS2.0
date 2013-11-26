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
	
	
}
