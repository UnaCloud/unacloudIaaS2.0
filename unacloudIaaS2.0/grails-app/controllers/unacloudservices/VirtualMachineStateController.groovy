package unacloudservices

import unacloudEnums.VirtualMachineExecutionStateEnum;
import back.services.BackPersistenceService;

class VirtualMachineStateController {
	BackPersistenceService backPersistenceService
    def updateVirtualMachineState() {
		String executionId=params['executionId']
		def stateString=params['state']
		def message=params['message']
		if(executionId!=null&&stateString!=null&&message!=null&&executionId.matches("[0-9]+")){
			backPersistenceService.updateVirtualMachineState(Long.parseLong(executionId),VirtualMachineExecutionStateEnum.valueOf(stateString),message)
		}
		render "successful"
	}
}
