package unacloud2

import org.codehaus.groovy.grails.resolve.config.RepositoriesConfigurer;

class PhysicalMachine {

    String name
	boolean withUser
	int cores
	int ram
	boolean highAvaliability
	String hypervisorPath
	IP ip
	String mac
	PhysicalMachineStateEnum state
	OperatingSystem operatingSystem
	
	static hasMany = [repositories: PhysicalMachineRepository]
	
	static belongsTo =[laboratory: Laboratory]
	static constraints = {
    }
	
	def VirtualMachine getFreeVirtualMachine(VirtualMachineImage image){
		VirtualMachine vm=null 
		for(PhysicalMachineRepository repo:repositories){
			vm=repo.getFreeVirtualMachine(image);
			if(vm!=null)return vm;
		}
		//TODO Falta crear la vm si no existe
		return vm;
	}
}
