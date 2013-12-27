package unacloudservices

import back.services.LogService;
import back.services.PhysicalMachineStateManagerService;
import back.services.VariableManagerService;

class UnaCloudServicesController {
	PhysicalMachineStateManagerService physicalMachineStateManagerService;
	VariableManagerService variableManagerService;
	LogService logService;
    def clouderClientAttention(){
		println params.type
		//physicalMachineStateManagerService.reportPhysicalMachine("", "")
		render "Succeded"
	}
	def agentVersion(){
		variableManagerService.getStringValue("AGENT_VERSION");
	}
	
	def updateAgentVersion(){
		variableManagerService.updateAgentVersion();
		redirect(uri: "/configuration");	
	}
	
	def agent(){
		def openAgain = new File('web-app/agent.zip')
		response.setContentType("application/zip")
		response.setHeader("Content-disposition", "filename=agent.zip")
		
		response.outputStream << openAgain.getBytes()
		response.outputStream.flush()
	}
	def logMessage(){
		def component=params['component']
		def message=params['message']
		def hostname=params['hostname']
		logService.createLog("",component,message);
	}
}
