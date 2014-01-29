package back.ipallocators

import back.services.PhysicalMachineStateManagerService;
import java.util.Comparator;
import unacloud2.*

class IpAllocatorService {
	def allocateIPAddresses(DeployedCluster cluster){
		for (DeployedImage im in cluster.images){
			for(VirtualMachineExecution vme in im.virtualMachines){
				IPPool ipPool= vme.executionNode.laboratory.virtualMachinesIPs
				println "Using node ip "+vme.executionNode.ip.ip
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
