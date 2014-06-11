package unacloud2

import org.codehaus.groovy.grails.resolve.config.RepositoriesConfigurer;

import unacloudEnums.VirtualMachineExecutionStateEnum;

class PhysicalMachine {

    String name
	boolean withUser
	int cores
	int ram
	boolean highAvailability
	String hypervisorPath
	IP ip
	String mac
	PhysicalMachineStateEnum state
	OperatingSystem operatingSystem
	Date lastReport
	Laboratory laboratory;
	
	def long getDatabaseId(){
		return id;
	}
	
}
