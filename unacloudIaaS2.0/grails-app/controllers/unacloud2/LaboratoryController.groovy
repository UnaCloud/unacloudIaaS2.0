package unacloud2

import back.services.AgentService;
import grails.converters.JSON

class LaboratoryController {
	
	//-----------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------
	
	/**
	 * Representation of laboratory services
	 */
	LaboratoryService laboratoryService
	
	/**
	 * Representation of agent services
	 */
	AgentService agentService
	
	//-----------------------------------------------------------------
	// Actions
	//-----------------------------------------------------------------
	
	
	/**
	 * Makes session verifications before executing any other action
	 */
	
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
	
	/**
	 * Laboratory index action
	 * @return list of all laboratories
	 */
    def index() { 
		[laboratories: Laboratory.list(params)]	
	}
	
	/**
	 * Laboratory physical machine index.
	 * @return laboratory selected and list of machines contained in it 
	 */
	def getLab() {
		def lab = Laboratory.get(params.id)
		def machineSet= lab.getOrderedMachines()
		[lab: Laboratory.get(params.id), machineSet:machineSet]
	}
	
	/**
	 * Create lab form action.
	 * @return list of network configurations
	 */
	def addLab(){
		def networkConfigurations = ['ETHERNET10MBPS','ETHERNET100MBPS','ETHERNET1GBPS']
		[netConfigurations: networkConfigurations]
	}
	
	/**
	 * Save created lab action. Redirects to index when finished 
	 */
	def createLab(){
		laboratoryService.createLab(params.name, (params.highAvailability!=null),params.netConfig, (params.virtual!=null),params.netGateway, params.netMask);
		redirect(action: "index");
	}
	
	/**
	 * Create physical machine form action
	 * @return selected laboratory an list of operating systems 
	 */
	def createMachine() {
		[lab: Laboratory.get(params.id),oss: OperatingSystem.list()]
	}
	
	/**
	 * Saves created physical machine and redirects to index
	 * @return
	 */
	def addMachine(){
		laboratoryService.addMachine(params.ip, params.name, params.cores, params.ram, params.disk, params.osId, params.mac, params.labId)
		redirect(action:"getLab", params:[id:params.labId])
	}
	
	/**
	 * Edit physical machine form action
	 * @return physical machine, laboratory which it belongs and list of all 
	 * operating systems
	 */
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
	
	/**
	 * Save changes in physical machine info. Redirects to indes when finished
	 * @return
	 */
	def setValues(){
		def machine = PhysicalMachine.get(params.id)
		laboratoryService.setValues(machine, params.name, params.ip, params.osId, params.cores, params.mac, params.ram, params.disk)
		redirect(action:"getLab", params:[id: params.labId])
	}
	
	/**
	 * Sends an update message to physical machine agents selected. Redirects to 
	 * indes when finished
	 */
	def updateMachines(){
		def resp;
		def cou = 0;		
		params.each {
			print it
			if (it.key.contains("machine")){
				if(it.value.contains("on")){
					PhysicalMachine pm= PhysicalMachine.get((it.key - "machine") as Integer)
					if(!agentService.updateMachine(pm)){
						cou++
					}
				}
			}
		}
		if(cou>0)resp = [success:false,'count':cou]
		else resp = [success:true]	
		println "Termine de actualizar"
		render resp as JSON
		
	}//redirect( action: "index")
	
	/**
	 * Stop agent in selected machines. Returns to index when finished
	 */
	
	def stopMachines(){
		def resp;
		def cou = 0;
		params.each {
			if (it.key.contains("machine")){
				if(it.value.contains("on")){
					PhysicalMachine pm= PhysicalMachine.get((it.key - "machine") as Integer)
					if(!agentService.stopMachine(pm)){
						cou++
					}
				}
			}
		}
		if(cou>0)resp = [success:false,'count':cou]
		else resp = [success:true]
		render resp as JSON
	}
	
	/**
	 * Clears all image files of selected physical machines. Returns to index when
	 * finished
	 */
	def clearCache(){
		println "clearCache"
		def resp;
		def cou = 0;
		params.each {
			if (it.key.contains("machine")){
				if(it.value.contains("on")){
					PhysicalMachine pm= PhysicalMachine.get((it.key - "machine") as Integer)					
					if(!agentService.clearCache(pm)){
						cou++
					}
				}
			}
		}
		if(cou>0)resp = [success:false,'count':cou]
		else resp = [success:true]
		render resp as JSON
	}
}
