package unacloudservices

import back.services.PhysicalMachineStateManagerService;

class UnaCloudServicesController {
	PhysicalMachineStateManagerService physicalMachineStateManagerService;
    def clouderClientAttention(){
		println params.type
		physicalMachineStateManagerService.reportPhysicalMachine("", "")
		//render ClouderClientAttention.attendRequest(request)
	}
	def agentVersion(){
		render "2.0.0"
	}
}
