package unacloudservices

import back.services.AgentService;
import back.services.LogService;
import back.services.PhysicalMachineStateManagerService;
import back.services.VariableManagerService;

class UnaCloudServicesController {
	PhysicalMachineStateManagerService physicalMachineStateManagerService;
	VariableManagerService variableManagerService;
	AgentService agentService;
	LogService logService;
    def clouderClientAttention(){
		println params.type
		//physicalMachineStateManagerService.reportPhysicalMachine("", "")
		render "Succeded"
	}
	def agentVersion(){
		render variableManagerService.getStringValue("AGENT_VERSION");
	}
	
	def changeServerVariables(){
		variableManagerService.changeServerVariables(params)
		redirect(uri: "/configuration");
	}
	
	def updateAgentVersion(){
		variableManagerService.updateAgentVersion();
		redirect(uri: "/configuration");	
	}
	
	def agent(){
		response.setContentType("application/zip")
		response.setHeader("Content-disposition", "filename=agent.zip")
		agentService.copyAgentOnStream(response.outputStream,grailsAttributes.getApplicationContext().getResource("/").getFile())
		response.outputStream.flush()
	}
	def updater(){
		response.setContentType("application/zip")
		response.setHeader("Content-disposition", "filename=updater.zip")
		agentService.copyUpdaterOnStream(response.outputStream,grailsAttributes.getApplicationContext().getResource("/").getFile())
		response.outputStream.flush()
	}
	def logMessage(){
		def component=params['component']
		def message=params['message']
		def hostname=params['hostname']
		logService.createLog("",component,message);
	}
}
