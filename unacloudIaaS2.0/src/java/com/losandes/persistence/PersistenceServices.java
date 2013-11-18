/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id$ SecurityServiceTest.java
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 * Licenciado bajo el esquema Academic Free License version 2.1
 *
 * Ejercicio: Muebles los Alpes
 * Autor: German Sotelo
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package com.losandes.persistence;
import com.losandes.beans.*;
import com.losandes.utils.VirtualMachineCPUStates;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.*;
import static com.losandes.utils.Constants.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Prueba la implementacion de la interfaz SecurityServiceTest proveida por la aplicación
 * @author German Sotelo
 */
public class PersistenceServices {

    EntityManager em;
    
    /**
     * Constructor de la prueba
     * Pide la instancia de la unidad de persistencia a la aplicación
     */
    public PersistenceServices(){
        
        em =Persistence.createEntityManagerFactory("UnacloudServicesPU").createEntityManager();
    }

    public void updatePhysicalMachineState(int state,String ... machineIds){
        try(Connection con=DatabaseConnection.getConnection();PreparedStatement st=con.prepareStatement("update `physicalmachine` set `PHYSICALMACHINESTATE`=? where `PHYSICALMACHINENAME`=?;")){
            for(String machineId:machineIds){
                st.setInt(1,state);
                st.setString(2,machineId);
                st.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersistenceServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void InsertNodeStateLog(String machineName, String option, String value ){
        NodeStateLog n = em.find(NodeStateLog.class, machineName);
        em.getTransaction().begin();
        if(n==null){
            n=new NodeStateLog();
            n.setPhysicalMachineName(machineName);
            em.persist(n);
            em.flush();
            return;
        }
        if (option.equals("ClientState"))
            n.setClientState(value);
        else if (option.equals("ServicesNotRunning"))
            n.setServicesNotRunning(value);
    }
    
    public void logginPhysicalMachineUser(String machineId, String user){
        if(user!=null&&user.equals("null"))user=null;
        try(Connection con=DatabaseConnection.getConnection();PreparedStatement st=con.prepareStatement("update `physicalmachine` set `PHYSICALMACHINESTATE`=?,`PHYSICALMACHINEUSER`=? where `PHYSICALMACHINENAME`=?;")){
            st.setInt(1,ON_STATE);
            st.setString(2,user);
            st.setString(3,machineId);
            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PersistenceServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateVirtualMachineState(String virtualMachineExecutionCode, int state, String message) {
        try(Connection con=DatabaseConnection.getConnection();){
            String physicalMachineName=null,virtualMachineCode=null;
            try(PreparedStatement st=con.prepareStatement("select `PHYSICALMACHINE_PHYSICALMACHINENAME`,vm.`VIRTUALMACHINECODE` from `virtualmachine` vm, `virtualmachineexecution` vme where vme.`VIRTUALMACHINE_VIRTUALMACHINECODE` = vm.`VIRTUALMACHINECODE` and vme.`VIRTUALMACHINEEXECUTIONCODE` = ?;")){
                 st.setString(1,virtualMachineExecutionCode);
                 try(ResultSet rs=st.executeQuery()){
                     if(rs.next()){
                         physicalMachineName=rs.getString(1);
                         virtualMachineCode=rs.getString(2);
                     }
                 }
            }
            try(PreparedStatement ps=con.prepareStatement("update `physicalmachine` set `PHYSICALMACHINESTATE` = ? where `PHYSICALMACHINENAME` = ?;")){
                ps.setInt(1,VM_TURN_ON);
                ps.setString(2,physicalMachineName);
                ps.executeUpdate();
            }
            switch(state){
                case ERROR_STATE:
                    try(PreparedStatement ps=con.prepareStatement("update `virtualmachine` set `TURNONCOUNT` = 0 where `VIRTUALMACHINECODE` = ?;")){
                        ps.setString(1,virtualMachineCode);
                        ps.executeUpdate();
                    }
                    break;
                case ON_STATE:
                    try(PreparedStatement ps=con.prepareStatement("update `virtualmachine` set `TURNONCOUNT` = `TURNONCOUNT`+1,`VIRTUALMACHINESTATE`=? where `VIRTUALMACHINECODE` = ?;")){
                        ps.setInt(1, ON_STATE);
                        ps.setString(2,virtualMachineCode);
                        ps.executeUpdate();
                    }
                    break;
            }
            try(PreparedStatement ps=con.prepareStatement("update `virtualmachineexecution` set `VIRTUALMACHINEEXECUTIONSTATUS` = ?,`VIRTUALMACHINEEXECUTIONSTATUSMESSAGE`=? where `VIRTUALMACHINEEXECUTIONCODE` = ?;")){
                ps.setInt(1,state);
                ps.setString(2,message);
                ps.setString(3,virtualMachineExecutionCode);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersistenceServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateVirtualMachineCPUState(Object virtualMachineExecutionCode, VirtualMachineCPUStates cpuState) {
        Virtualmachineexecution vme = (Virtualmachineexecution)em.find(Virtualmachineexecution.class,virtualMachineExecutionCode);
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
        System.out.println(virtualMachineExecutionCode+" is "+cpuState);
    }

    public String[] getAllPhysicalMachines() {
        List l = em.createNativeQuery("SELECT pm.* FROM PhysicalMachine pm ;",Physicalmachine.class).setHint("toplink.refresh", "true").getResultList();
        String[] ret = new String[l.size()];
        for(int e=0;e<l.size();e++)ret[e]=((Physicalmachine)l.get(e)).getPhysicalmachinename();
        return ret;
    }



}