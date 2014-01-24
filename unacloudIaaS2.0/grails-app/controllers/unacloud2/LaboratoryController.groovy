package unacloud2

import back.services.AgentService;

class LaboratoryController {
	
	LaboratoryService laboratoryService
	AgentService agentService
	
	def beforeInterceptor = {
		if(!session.user){
			flash.message="You must log in first"
			redirect(uri:"/login", absolute:true)
			return false
		}
		else if(!(session.user.userType.equals("Administrator"))){
			flash.message="You must be administrator to see this content"
			redirect(uri:"/error", absolute:true)
			return false
		}
	}
	
    def index() { 
		[laboratories: Laboratory.list(params)]	
	}
	def getLab() {
		def lab = Laboratory.get(params.id)
		def machineSet= lab.getOrderedMachines()
		[lab: Laboratory.get(params.id), machineSet:machineSet]
	}
	def createMachine() {
		[lab: Laboratory.get(params.id),oss: OperatingSystem.list()]
	}
	def addMachine(){
		laboratoryService.addMachine(params.ip, params.name, params.cores, params.ram, params.disk, params.hyperPath, params.osId, params.mac, params.labId)
		redirect(action:"getLab", params:[id:params.labId])
	}
	def editMachine(){
		def machine = PhysicalMachine.get(params.id)
		def lab = Laboratory.get(params.labId)
		if (!machine || !lab) {
			redirect(action:"index")
		}
		else{
			[machine: machine, lab: lab, oss:OperatingSystem.list()]
		}
	}
	
	def setValues(){
		def machine = PhysicalMachine.get(params.id)
		laboratoryService.setValues(machine, params.name, params.ip, params.osId, params.cores, params.mac, params.ram, params.disk, params.hyperPath)
		redirect(action:"getLab", params:[id: params.labId])
	}
	
	def updateMachines(){
		params.each {
			if (it.key.contains("machine")){
				if(it.value.contains("on")){
					PhysicalMachine pm= PhysicalMachine.get((it.key - "machine") as Integer)
					agentService.updateMachine(pm)
				}
			}
		}
		println "Termine de actualizar"
		redirect( action: "index")
	}
	def stopMachines(){
		params.each {
			if (it.key.contains("machine")){
				if(it.value.contains("on")){
					PhysicalMachine pm= PhysicalMachine.get((it.key - "machine") as Integer)
					agentService.stopMachine(pm)
				}
			}
		}
		redirect( action: "index")
	}
	def clearCache(){
		params.each {
			if (it.key.contains("machine")){
				if(it.value.contains("on")){
					PhysicalMachine pm= PhysicalMachine.get((it.key - "machine") as Integer)
					agentService.clearCache(pm)
				}
			}
		}
		redirect( action: "index")
	}
}
