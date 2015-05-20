package back.userRestrictions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.SessionCookieConfig;

import org.springframework.aop.ThrowsAdvice;

import unacloud2.DeployedCluster;
import unacloud2.PhysicalMachine;
import unacloud2.User;
import unacloud2.Laboratory;
import unacloud2.UserRestriction;
import unacloud2.VirtualMachineExecution;

import unacloud2.PhysicalMachineStateEnum;

class UserRestrictionProcessorService {
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------
	
	/**
	 * Applies the list of user restrictions to the configured deployment. The
	 * list of restrictions is taken form the UserRestrictionEnum  
	 * @param user session user in order to apply restrictions
	 * @param vms list of virtual machines and their properties
	 * @param pms list of physical machines and their properties
	 * @throws UserRestrictionException if the user is not allowed to 
	 * make the configured deployment
	 */
	
	def applyUserPermissions(User user,ArrayList<VirtualMachineExecution> vms,List<PhysicalMachine> pms) throws UserRestrictionException{
		for (UserRestriction ur:user.restrictions){
			UserRestrictionEnum value=UserRestrictionEnum.valueOf(ur.name);
			if(value!=null){
				println "aplying restriction "+value
				value.getUserRestriction().applyRestriction(ur.value,vms,pms);
			}
		}
	}
	
	def getAvoidedMachines(User user){
		UserRestriction rest = user.restrictions.find{it.name==UserRestrictionEnum.ALLOWED_LABS.name}
		if(rest!=null&&rest.name!=null&&!rest.name.isEmpty()){
			String[] values= rest.name.split(":");
			return PhysicalMachine.where{state == PhysicalMachineStateEnum.ON && laboratory.name in values}
		}else return PhysicalMachine.findAllByState(PhysicalMachineStateEnum.ON)		
	}
	
	def getAvoidedLabs(User user){
		UserRestriction rest = user.restrictions.find{it.name==UserRestrictionEnum.ALLOWED_LABS.name}
		if(rest!=null&&rest.name!=null&&!rest.name.isEmpty()){
			String[] values= rest.name.split(":");
			return Laboratory.where{name in values}
		}else return Laboratory.findAll();
	}
	
}
