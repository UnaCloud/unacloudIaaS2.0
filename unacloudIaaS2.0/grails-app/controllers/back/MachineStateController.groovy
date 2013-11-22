package back

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

    def reportPhysicalMachine(){
		String hostname=params['hostname'],hostuser=params['hostuser'];
		PhysicalMachineStateManagerService.reportPhysicalMachine(hostname,hostuser);
	}
	def reportPhysicalMachineState(){
		String strOperation=params['operation']
		String hostname=params['hostname']
		if(strOperation.matches("[0-9]+")){
			int operation=Integer.parseInt(strOperation);
			switch (operation) {
				case TURN_ON_DB:
					persistence.updatePhysicalMachineState(ON_STATE,hostname);
					break;
				case TURN_OFF_DB:
					persistence.updatePhysicalMachineState(OFF_STATE,hostname);
					break;
				case LOGIN_DB:
					String hostuser=params['hostuser']
					persistence.logginPhysicalMachineUser(hostname,hostuser);
					machineManager.reportMachine(hostname);
					break;
				case LOGOUT_DB:
					persistence.logginPhysicalMachineUser(hostname, NOTHING_AVAILABLE);
					break;
				case VIRTUAL_MACHINE_STATE_DB:
					/*persistence.updateVirtualMachineState(clouderServerRequest.getString(3),clouderServerRequest.getInteger(4),clouderServerRequest.getString(5));*/
					machineManager.reportMachine(hostname);
					break;
				case VIRTUAL_MACHINE_CPU_STATE:
					persistence.updateVirtualMachineCPUState(clouderServerRequest.getString(3),VirtualMachineCPUStates.valueOf(clouderServerRequest.getString(4)));
					break;
			}
			
		}
	}
}
