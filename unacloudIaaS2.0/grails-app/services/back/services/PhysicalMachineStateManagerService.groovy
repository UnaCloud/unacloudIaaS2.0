package back.services

import unacloud2.PhysicalMachine;
import unacloud2.PhysicalMachineStateEnum;

class PhysicalMachineStateManagerService {

    def reportPhysicalMachine(String hostname,String hostUser){
		PhysicalMachine pm=PhysicalMachine.findBy(name:hostname)
		Date currentTime=new Date();
		if(pm.state!=PhysicalMachineStateEnum.DISABLED&&pm.lastReport!=null&&(currentTime.getTime()-pm.lastReport.getTime())>5*60000)pm.state=PhysicalMachineStateEnum.OFF;
		pm.lastReport=currentTime;
		pm.save()
	}
}
