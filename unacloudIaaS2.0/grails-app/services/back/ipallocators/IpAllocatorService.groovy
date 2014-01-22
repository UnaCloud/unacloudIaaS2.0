package back.ipallocators

import back.services.PhysicalMachineStateManagerService;
import java.util.Comparator;
import unacloud2.*

class IpAllocatorService {
	def allocateIPAddresses(DeployedCluster cluster){
		for (DeployedImage im:cluster.images){
			for(VirtualMachineExecution vme:im.virtualMachines){
				println "Allocando en : "+vme.executionNode.ip.ip;
				IPPool ipPool= vme.executionNode.laboratory.virtualMachinesIPs
				for(ip in ipPool.ips){
					if(ip.used==false){
						vme.ip= ip
						ip.used=true
						break
					}
				}
			}
		}
	}
}
