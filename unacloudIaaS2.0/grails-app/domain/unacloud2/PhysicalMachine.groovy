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
	
	static belongsTo =[laboratory: Laboratory]
	static constraints = {
    }
	
}
