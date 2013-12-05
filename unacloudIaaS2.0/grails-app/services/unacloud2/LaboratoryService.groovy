package unacloud2

class LaboratoryService {

    def addMachine(ip, name, cores, ram, disk, hyperPath, osId, mac, labId) {
		def machineIP=new IP(ip:ip, used:true)
		machineIP.save(failOnError:true)
		def physicalMachine = new PhysicalMachine( lastReport: new Date(),name:name, state: PhysicalMachineStateEnum.OFF, cores:cores,
			ram: ram, hardDisk: disk, highAvaliability:false, hypervisorPath:hyperPath,
			ip:machineIP, operatingSystem: OperatingSystem.get(osId), mac:mac)
		def lab= Laboratory.get(labId)
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
}
