package back.allocators

import back.pmallocators.AllocatorException
import back.services.PhysicalMachineStateManagerService;
import java.util.Comparator;
import unacloud2.*

class IpAllocatorService {
	
	def allocateIPAddresses(DeployedImage image, boolean addInstancesDeployment){
		if(!addInstancesDeployment){
			for(VirtualMachineExecution vme in image.virtualMachines){
				IPPool ipPool= vme.executionNode.laboratory.virtualMachinesIPs
				println "Using node ip "+vme.executionNode.ip.ip
				
				for(ip in ipPool.ips){
					println "Verifying IP "+ ip.ip+" used: "+ip.used
					if(ip.used==false){
						println "Using vm ip "+ip.ip
						vme.ip= ip
						ip.used=true
						String[] subname= ip.ip.split("\\.")
						vme.name= vme.name+subname[2]+subname[3]
						break
					}
				}
				if (vme.ip==null){ 
					for(VirtualMachineExecution vm in image.virtualMachines){
						if(vm.ip!=null)	vm.ip.used=false
					}
					throw new AllocatorException("Not enough IPs for this deployment")
				}
			}
			
		}
		else{
			for(VirtualMachineExecution vme in image.virtualMachines){
				if(vme.message.equals("Adding instance")){
					IPPool ipPool= vme.executionNode.laboratory.virtualMachinesIPs
					println "Using node ip "+vme.executionNode.ip.ip
					for(ip in ipPool.ips){
						if(ip.used==false){
							vme.ip= ip
							ip.used=true
							String[] subname= ip.ip.split("\\.")
							vme.name= vme.name+subname[2]+subname[3]
							break
						}
					}
					if (vme.ip==null){ 
						for(VirtualMachineExecution vm in image.virtualMachines){
							if(vm.ip!=null)	vm.ip.used=false
						}
						throw new AllocatorException("Not enough IPs for this deployment")
					}
				}
			}
		}
	}
}
