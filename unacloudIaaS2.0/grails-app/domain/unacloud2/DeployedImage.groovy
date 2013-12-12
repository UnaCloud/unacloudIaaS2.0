package unacloud2

import java.util.ArrayList;

import unacloudEnums.VirtualMachineExecutionStateEnum;

class DeployedImage {
	VirtualMachineImage image
	
	boolean highAvaliavility
	
	static hasMany = [virtualMachines: VirtualMachineExecution]
    
	static constraints = {
		image nullable:true 
    }
	
	ArrayList <VirtualMachineExecution> getOrderedVMs(){
		VirtualMachineComparator c= new VirtualMachineComparator()
		ArrayList <VirtualMachineExecution> array = new ArrayList(virtualMachines).sort(c)
		return array
	}
	
	def numberOfActiveMachines(){
		def counter=0
		virtualMachines.each {
			if (!(it.status==VirtualMachineExecutionStateEnum.FINISHED))
			counter++
		}
		return counter
	}
	
	def getDeployedRAM(){
		for(virtualMachine in virtualMachines){
			return virtualMachine.ram
		}
	}
	
	def getDeployedCores(){
		for(virtualMachine in virtualMachines){
			return virtualMachine.cores
		}
	}
	
	
	
}
