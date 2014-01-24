package unacloudservices

import java.util.Date;

import javassist.bytecode.stackmap.BasicBlock.Catch;
import unacloud2.IP;
import unacloud2.OperatingSystem;
import unacloud2.PhysicalMachineStateEnum;

import com.losandes.utils.VirtualMachineCPUStates;

import back.services.PhysicalMachineStateManagerService;

class MachineStateController {
	PhysicalMachineStateManagerService physicalMachineStateManagerService;
	def physicalMachineStart(){
		String hostname=params['hostname']
		physicalMachineStateManagerService.reportPhysicalMachine(hostname,null)
		render "succeeded"
	}
	def physicalMachineStop(){
		String hostname=params['hostname']
		physicalMachineStateManagerService.turnOffPhysicalMachine(hostname)
		render "succeeded"
	}
	def physicalMachineLogoff(){
		String hostname=params['hostname']
		physicalMachineStateManagerService.reportPhysicalMachine(hostname,null)
		render "succeeded"
	}
	def reportPhysicalMachineLogin(){
		String hostname=params['hostname']
		String hostuser=params['hostuser']
		try{
			physicalMachineStateManagerService.reportPhysicalMachine(hostname,hostuser)
			println "Exito on reportPhysicalMachineLogin "+ hostname+" "+hostuser;
		}catch(Exception ex){
			println "  Error on reportPhysicalMachineLogin "+ hostname+" "+hostuser;
			//ex.printStackTrace();
		}
		
		render "succeeded"
	}
	def registerPhysicalMachine(){
		String hostname=params['hostname']
		String cores=params['cores']
		String ram=params['ram']
		String mac=params['mac']
		String operatingSystem=params['os']
	}
}
