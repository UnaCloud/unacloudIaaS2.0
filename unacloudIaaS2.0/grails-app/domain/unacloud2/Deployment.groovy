package unacloud2

import unacloudEnums.VirtualMachineExecutionStateEnum;

class Deployment {
	//-----------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------
	
	/**
	 * Deployed cluster representation 
	 */
	DeployedCluster cluster
	
	/**
	 * start time of the deployment
	 */
	Date startTime
	
	/**
	 * stop time of the deployment
	 */
	Date stopTime
	
	/**
	 * present status of the deployment (ACTIVE of FINISHED)
	 */
	DeploymentStateEnum status
	
	static constraints = {	
    }
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------
	
	/**
	 * Counts all deployment active virtual machines
	 * @return number of active virtual machines in this deployment
	 */
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
	
	/**
	 * Refresh the deployment status verifying all nodes
	 */
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
	
	/**
	 * Verifies and refresh the deployment status
	 * @return if the deployment is active or not after refreshing
	 */
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
