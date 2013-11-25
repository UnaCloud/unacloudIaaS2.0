package back.services

import unacloud2.PhysicalMachine;
import unacloud2.PhysicalMachineStateEnum;

class PhysicalMachineStateManagerService {

    def reportPhysicalMachine(String hostname,String hostUser){
		PhysicalMachine pm=PhysicalMachine.findByName(hostname)
		if(pm!=null){
			Date currentTime=new Date();
			if(pm.state!=PhysicalMachineStateEnum.DISABLED){
				pm.state=PhysicalMachineStateEnum.ON;
				pm.lastReport=currentTime;
				pm.save()
			}
			
		}
	}
	def reportPhysicalMachine(String hostname){
		PhysicalMachine pm=PhysicalMachine.findByName(hostname)
		if(pm!=null){
			Date currentTime=new Date();
			if(pm.state!=PhysicalMachineStateEnum.DISABLED){
				pm.state=PhysicalMachineStateEnum.ON;
				pm.lastReport=currentTime;
				pm.save()
			}
		}
	}
	def turnOffPhysicalMachine(String hostname){
		PhysicalMachine pm=PhysicalMachine.findByName(hostname)
		if(pm!=null&&pm.state!=PhysicalMachineStateEnum.DISABLED){
			pm.state=PhysicalMachineStateEnum.OFF;
			pm.save()
		}
		
	}
}
