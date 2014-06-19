package back.services

import pmStateManager.StateManager;
import unacloud2.PhysicalMachine;
import unacloud2.PhysicalMachineStateEnum;

class PhysicalMachineStateManagerService {

    def reportPhysicalMachine(String hostname,String hostUser,String requestAddress){
		boolean update=StateManager.registerPhysicalMachineReport(hostname, hostUser);
		if(update){
			PhysicalMachine.executeUpdate("update PhysicalMachine m set m.state=:newState, m.withUser=:newWithUser,m.lastReport=:time where m.name=:pmname",
				[time: new Date(), newState: PhysicalMachineStateEnum.ON, newWithUser: (hostUser!=null&&!hostUser.isEmpty()&&!(hostUser.replace(">","").replace(" ","")).equals("null")), pmname:hostname])
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
