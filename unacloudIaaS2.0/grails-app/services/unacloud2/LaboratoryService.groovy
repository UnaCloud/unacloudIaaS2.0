package unacloud2

class LaboratoryService {

    def addMachine(ip, name, cores, ram, disk, hyperPath, osId, mac, labId) {
		def machineIP=new IP(ip:ip, used:true)
		machineIP.save(failOnError:true)
		def lab= Laboratory.get(labId)
		println "creating machine in laboratory"+ lab.name+"-"+lab.highAvailability
		def physicalMachine = new PhysicalMachine( lastReport: new Date(),name:name, state: PhysicalMachineStateEnum.OFF, cores:cores,
			ram: ram, hardDisk: disk, highAvailability:(lab.highAvailability), hypervisorPath:hyperPath,
			ip:machineIP, operatingSystem: OperatingSystem.get(osId), mac:mac)
		
		physicalMachine.laboratory=lab
		physicalMachine.save(failOnError:true)
		lab.physicalMachines.add(physicalMachine)
		lab.save()
    }
	
	def setValues(PhysicalMachine machine, name, ip,osId,cores,mac,ram,disk,hyperPath){
		machine.putAt("name", name)
		if(!machine.ip.ip.equals(ip)){
			machine.ip.delete()
			def newIp= new IP(ip:ip, used:true)
			newIp.save()
			machine.putAt("ip", newIp)
		}
		machine.putAt("operatingSystem", OperatingSystem.get(osId))
		machine.putAt("cores", Integer.parseInt(cores))
		machine.putAt("mac", mac)
		machine.putAt("ram", Integer.parseInt(ram))
		machine.putAt("hypervisorPath", hyperPath)
	}
	
	def createLab(name, highAvailability,netConfig, virtual, netGateway, netMask){
		def ipPool=new IPPool(virtual:virtual,gateway: netGateway, mask: netMask).save()
		new Laboratory (virtualMachinesIPs: ipPool, name: name, highAvailability: highAvailability,networkQuality: netConfig ).save();
	}
}
