package back.services;

import static com.losandes.utils.Constants.ERROR_STATE;
import static com.losandes.utils.Constants.OFF_STATE;
import static com.losandes.utils.Constants.ON_STATE;
import static com.losandes.utils.Constants.VM_TURN_ON;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import unacloud2.AllocationPolicy;
import unacloud2.DeployedCluster;
import groovy.sql.Sql;
import com.losandes.utils.VirtualMachineCPUStates;
/**
 * Prueba la implementacion de la interfaz SecurityServiceTest proveida por la aplicación
 * @author German Sotelo
 */
class BackPersistenceServices {
	
	static transactional = false
	
	def dataSource

    def serviceMethod() {

    }
	
	def updatePhysicalMachineState(int state,String ... machineIds){
		def db = new Sql(dataSource);
		Connection con=db.getConnection();
		try{
			PreparedStatement st=con.prepareStatement("update `physicalmachine` set `PHYSICALMACHINESTATE`=? where `PHYSICALMACHINENAME`=?;");
			for(String machineId:machineIds){
				st.setInt(1,state);
				st.setString(2,machineId);
				st.executeUpdate();
			}
		} catch (SQLException ex) {
			Logger.getLogger(PersistenceServices.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	def logginPhysicalMachineUser(String machineId, String user){
		if(user!=null&&user.equals("null"))user=null;
		def db = new Sql(dataSource);
		Connection con=db.getConnection();
		try{
			PreparedStatement st=con.prepareStatement("update `physicalmachine` set `PHYSICALMACHINESTATE`=?,`PHYSICALMACHINEUSER`=? where `PHYSICALMACHINENAME`=?;");
			st.setInt(1,ON_STATE);
			st.setString(2,user);
			st.setString(3,machineId);
			st.executeUpdate();
		} catch (SQLException ex) {
			Logger.getLogger(PersistenceServices.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	def updateVirtualMachineState(String virtualMachineExecutionCode, int state, String message) {
		def db = new Sql(dataSource);
		Connection con=db.getConnection();
		try{
			String physicalMachineName=null,virtualMachineCode=null;
				PreparedStatement st=con.prepareStatement("select `PHYSICALMACHINE_PHYSICALMACHINENAME`,vm.`VIRTUALMACHINECODE` from `virtualmachine` vm, `virtualmachineexecution` vme where vme.`VIRTUALMACHINE_VIRTUALMACHINECODE` = vm.`VIRTUALMACHINECODE` and vme.`VIRTUALMACHINEEXECUTIONCODE` = ?;");
				st.setString(1,virtualMachineExecutionCode);
					ResultSet rs=st.executeQuery();
					if(rs.next()){
						physicalMachineName=rs.getString(1);
						virtualMachineCode=rs.getString(2);
					}
				PreparedStatement ps2=con.prepareStatement("update `physicalmachine` set `PHYSICALMACHINESTATE` = ? where `PHYSICALMACHINENAME` = ?;");
				ps2.setInt(1,VM_TURN_ON);
				ps2.setString(2,physicalMachineName);
				ps2.executeUpdate();
			switch(state){
				case ERROR_STATE:
						PreparedStatement ps=con.prepareStatement("update `virtualmachine` set `TURNONCOUNT` = 0 where `VIRTUALMACHINECODE` = ?;");
						ps.setString(1,virtualMachineCode);
						ps.executeUpdate();
					break;
				case ON_STATE:
						PreparedStatement ps=con.prepareStatement("update `virtualmachine` set `TURNONCOUNT` = `TURNONCOUNT`+1,`VIRTUALMACHINESTATE`=? where `VIRTUALMACHINECODE` = ?;");
						ps.setInt(1, ON_STATE);
						ps.setString(2,virtualMachineCode);
						ps.executeUpdate();
					break;
			}
				PreparedStatement ps=con.prepareStatement("update `virtualmachineexecution` set `VIRTUALMACHINEEXECUTIONSTATUS` = ?,`VIRTUALMACHINEEXECUTIONSTATUSMESSAGE`=? where `VIRTUALMACHINEEXECUTIONCODE` = ?;");
				ps.setInt(1,state);
				ps.setString(2,message);
				ps.setString(3,virtualMachineExecutionCode);
				ps.executeUpdate();
		} catch (Exception ex) {
			Logger.getLogger(PersistenceServices.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void updateVirtualMachineCPUState(Object virtualMachineExecutionCode, VirtualMachineCPUStates cpuState) {
		/*Virtualmachineexecution vme = (Virtualmachineexecution)em.find(Virtualmachineexecution.class,virtualMachineExecutionCode);
		if(vme!=null){
			em.getTransaction().begin();
			vme.getVirtualmachine().setCpustate(cpuState.ordinal());
			em.merge(vme.getVirtualmachine());
			em.flush();
			em.getTransaction().commit();
			Elasticrule er = vme.getTemplate().getElasticrule();
			if(er!=null&&er.getActive()){
				Collection<Virtualmachine> vms = vme.getTemplate().getVirtualmachineCollection();
				int busy=0,on=0;
				for(Virtualmachine vm:vms){
					if(vm.getVirtualmachinestate()!=OFF_STATE){
						on++;
						if(vm.getCpustate().intValue()==VirtualMachineCPUStates.BUSY.ordinal())busy++;
					}
				}
				int perc=((busy*100)/on);
				System.out.println("busy %= "+busy+"/"+on);
				if(er.getLowerlimit()>perc){
					int t = 0;
					if(er.getAddition())t=er.getFactor().intValue();
					else if(er.getMultiply())t=(int)(on/er.getFactor());
					t=Math.min(on,t);

				}else if(er.getUpperlimit()<perc){
					int t = 0;
					if(er.getAddition())t=er.getFactor().intValue();
					else if(er.getMultiply()){
						t=(int)(on*er.getFactor())-on;
					}
					t=Math.min(vms.size()-on,t);
					System.out.println("Prender "+t);
					//UnaCloudOperations.turnOnVirtualCluster(vme.getSystemuser().getSystemusername(),"",vme.getTemplate().getTemplatecode(),t,vme.getVirtualmachineexecutionrammemory(),vme.getVirtualmachineexecutioncores(),0,20);
				}
			}
			
		}
		System.out.println(virtualMachineExecutionCode+" is "+cpuState);*/
	}

	public String[] getAllPhysicalMachines() {
		List l = em.createNativeQuery("SELECT pm.* FROM PhysicalMachine pm ;",Physicalmachine.class).setHint("toplink.refresh", "true").getResultList();
		String[] ret = new String[l.size()];
		//for(int e=0;e<l.size();e++)ret[e]=((Physicalmachine)l.get(e)).getPhysicalmachinename();
		return ret;
	}


}
