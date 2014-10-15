package back.services

import pmStateManager.StateManager;
import unacloud2.DeploymentService
import unacloud2.PhysicalMachine;
import unacloud2.PhysicalMachineStateEnum;
import unacloud2.VirtualMachineExecution;

class PhysicalMachineStateManagerService {
	
	DeploymentService deploymentService
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------
	
	/**
	 * Receives reports from UnaCloud agent and updates database with the new
	 * information
	 * @param hostname physical machine hostname
	 * @param hostUser physical machine logged in user
	 * @param requestAddress physical machine address
	 */
    def reportPhysicalMachine(String hostname,String hostUser,String requestAddress){
		boolean update=StateManager.registerPhysicalMachineReport(hostname, hostUser);
		if(update){
			PhysicalMachine.executeUpdate("update PhysicalMachine m set m.state=:newState, m.withUser=:newWithUser,m.lastReport=:time where m.name=:pmname",
				[time: new Date(), newState: PhysicalMachineStateEnum.ON, newWithUser: (hostUser!=null&&!hostUser.isEmpty()&&!(hostUser.replace(">","").replace(" ","")).equals("null")), pmname:hostname])
		}
		
	}
	
	/**
	 * Stops all the virtual machines allocated on the given node 
	 * @param hostanme physical machine hostname
	 * @return
	 */
	def stopVirtualMachines(String hostname){
		PhysicalMachine pm=PhysicalMachine.findByName(hostname)
		if(pm!=null&&pm.state==PhysicalMachineStateEnum.ON ){
			def vms = VirtualMachineExecution.findAllWhere(executionNode: pm)
			for (vm in vms){
				deploymentService.stopVirtualMachineExecution(vm)
			}
		}
	}
	/**
	 * Changes a physical machine state to 'OFF'
	 * @param hostname physical machine hostname
	 */
	def turnOffPhysicalMachine(String hostname){
		PhysicalMachine pm=PhysicalMachine.findByName(hostname)
		if(pm!=null&&pm.state!=PhysicalMachineStateEnum.DISABLED){
			pm.putAt("state", PhysicalMachineStateEnum.OFF)
			pm.save()
		}
		
	}
}
