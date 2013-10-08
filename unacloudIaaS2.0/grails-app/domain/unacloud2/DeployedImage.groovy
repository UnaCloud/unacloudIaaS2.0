package unacloud2

import java.util.ArrayList;

class DeployedImage {
	VirtualMachineImage image
	static hasMany = [virtualMachines: VirtualMachine]
    static constraints = {
    }
	
	ArrayList <VirtualMachine> getOrderedVMs(){
		VirtualMachineComparator c= new VirtualMachineComparator()
		ArrayList <VirtualMachine> array = new ArrayList(virtualMachines).sort(c)
		return array
	}
	
	def numberOfActiveMachines(){
		def counter=0
		virtualMachines.each {
			if (!(it.status==VirtualMachine.FINISHED))
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
