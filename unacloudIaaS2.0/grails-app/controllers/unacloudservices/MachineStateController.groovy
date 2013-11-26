package unacloudservices

import static com.losandes.utils.Constants.LOGIN_DB;
import static com.losandes.utils.Constants.LOGOUT_DB;
import static com.losandes.utils.Constants.NOTHING_AVAILABLE;
import static com.losandes.utils.Constants.OFF_STATE;
import static com.losandes.utils.Constants.ON_STATE;
import static com.losandes.utils.Constants.TURN_OFF_DB;
import static com.losandes.utils.Constants.TURN_ON_DB;
import static com.losandes.utils.Constants.VIRTUAL_MACHINE_CPU_STATE;
import static com.losandes.utils.Constants.VIRTUAL_MACHINE_STATE_DB;

import com.losandes.utils.VirtualMachineCPUStates;

import back.services.PhysicalMachineStateManagerService;

class MachineStateController {
	PhysicalMachineStateManagerService physicalMachineStateManagerService;
	def reportPhysicalMachineState(){
		String strOperation=params['operation']
		String hostname=params['hostname']
		println "reportPhysicalMachineState "+strOperation+" "+hostname
		if(strOperation.matches("[0-9]+")){
			int operation=Integer.parseInt(strOperation);
			switch (operation) {
				case TURN_ON_DB:
					physicalMachineStateManagerService.reportPhysicalMachine(hostname)
					break;
				case TURN_OFF_DB:
					physicalMachineStateManagerService.turnOffPhysicalMachine(hostname)
					break;
				case LOGIN_DB:
					String hostuser=params['hostuser']
					physicalMachineStateManagerService.reportPhysicalMachine(hostname,hostuser)
					break;
				case LOGOUT_DB:
					physicalMachineStateManagerService.reportPhysicalMachine(hostname,null)
					break;
				case VIRTUAL_MACHINE_STATE_DB:
					//TODO no se que hace.
					//persistence.updateVirtualMachineState(clouderServerRequest.getString(3),clouderServerRequest.getInteger(4),clouderServerRequest.getString(5));
					//machineManager.reportMachine(hostname);
					break;
				case VIRTUAL_MACHINE_CPU_STATE:
					//persistence.updateVirtualMachineCPUState(clouderServerRequest.getString(3),VirtualMachineCPUStates.valueOf(clouderServerRequest.getString(4)));
					break;
			}
			render "succeeded"
		}
	}
}
