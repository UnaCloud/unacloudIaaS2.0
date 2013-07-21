package unacloud2

class LaboratoryController {
	
	def beforeInterceptor = {
		if(!session.user){
			flash.message="You must log in first"
			redirect(uri:"/", absolute:true)
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
		def ip=new IP(ip:params.ip, used:true)
		ip.save()
		def physicalMachine = new PhysicalMachine( name:params.name, slots:2, cores:params.cores, 
			ram: params.ram, hardDisk: params.disk, highAvaliability:false, hypervisorPath:params.hyperPath,
			ip:ip, operatingSystem: OperatingSystem.get(params.osId), mac:params.mac)
		physicalMachine.save()
		Laboratory.get(params.labId).physicalMachines.add(physicalMachine)
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
		machine.putAt("name", params.name)
		if(!machine.ip.ip.equals(params.ip)){
			machine.ip.delete()
			def newIp= new IP(ip:params.ip, used:true)
			newIp.save()
			machine.putAt("ip", newIp)
		}
		machine.putAt("operatingSystem", OperatingSystem.get(params.osId))
		machine.putAt("cores", Integer.parseInt(params.cores))
		machine.putAt("mac", params.mac)
		machine.putAt("ram", Integer.parseInt(params.ram))
		machine.putAt("hardDisk", Integer.parseInt(params.disk))
		machine.putAt("hypervisorPath", params.hyperPath)
		redirect(action:"getLab", params:[id: params.labId])
	}
	
	
}
