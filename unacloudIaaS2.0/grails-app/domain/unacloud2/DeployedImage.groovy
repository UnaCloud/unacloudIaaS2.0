package unacloud2

import java.util.ArrayList;

class DeployedImage {
	VirtualMachineImage image
	
	boolean highAvaliavility
	
	static hasMany = [virtualMachines: VirtualMachineExecution]
    static constraints = {
    }
	
	ArrayList <VirtualMachineExecution> getOrderedVMs(){
		VirtualMachineComparator c= new VirtualMachineComparator()
		ArrayList <VirtualMachineExecution> array = new ArrayList(virtualMachines).sort(c)
		return array
	}
	
	def numberOfActiveMachines(){
		def counter=0
		virtualMachines.each {
			if (!(it.status==VirtualMachineExecution.FINISHED))
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
