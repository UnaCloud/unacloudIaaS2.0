package unacloud2

class HypervisorService {

    def addHypervisor(name, hyperVersion) {
		def h= new Hypervisor(name:name, hypervisorVersion: hyperVersion)
		h.save()
    }
	
	def deleteHypervisor(Hypervisor hypervisor){
		hypervisor.delete()
	}
	
	def setValues(Hypervisor hypervisor, name){
		hypervisor.putAt("name", name)
		
	}
}
