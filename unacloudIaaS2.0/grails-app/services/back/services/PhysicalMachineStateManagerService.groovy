package back.services

import pmStateManager.StateManager;
import unacloud2.PhysicalMachine;
import unacloud2.PhysicalMachineStateEnum;

class PhysicalMachineStateManagerService {

    def reportPhysicalMachine(String hostname,String hostUser){
		boolean update=StateManager.registerPhysicalMachineReport(hostname, hostUser);
		//println "reportPhysicalMachine "+hostname+" \""+hostUser+"\" "+((hostUser!=null&&!hostUser.isEmpty()&&!(hostUser.replace(">","").replace(" ","")).equals("null")));
		if(update){
			PhysicalMachine pm=PhysicalMachine.findByName(hostname)
			if(pm!=null){
				Date currentTime=new Date();
				if(pm.state!=PhysicalMachineStateEnum.DISABLED){
					pm.state=PhysicalMachineStateEnum.ON;
					pm.lastReport=currentTime;
					pm.withUser=(hostUser!=null&&!hostUser.isEmpty()&&!(hostUser.replace(">","").replace(" ","")).equals("null"));
					pm.save()
				}
				
			}
		}
		
	}
	def turnOffPhysicalMachine(String hostname){
		PhysicalMachine pm=PhysicalMachine.findByName(hostname)
		if(pm!=null&&pm.state!=PhysicalMachineStateEnum.DISABLED){
			pm.putAt("state", PhysicalMachineStateEnum.OFF)
			pm.save()
		}
		
	}
}
