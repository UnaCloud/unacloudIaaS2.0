package com.losandes.physicalmachine;

import com.losandes.persistence.entity.ExecutionSlot;
import com.losandes.persistence.entity.PhysicalMachine;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Map;
import javax.ejb.Local;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Interface responsible for exposing the Physical Machine services
 */
@Local
public interface IPhysicalMachineServices {

    public String createOrUpdatePhysicalMachine(com.losandes.persistence.entity.PhysicalMachine machine);

    public boolean deletePhysicalMachine(String physicalMachineName);

    public java.util.List getPhysicalMachines();

    public java.lang.String turnOffPhysicalMachine(java.util.ArrayList<com.losandes.persistence.entity.PhysicalMachine> physicalMachines);

    public java.lang.String restartPhysicalMachine(java.util.ArrayList<com.losandes.persistence.entity.PhysicalMachine> physicalMachines);

    public java.lang.String logoutPhysicalMachine(java.util.ArrayList<com.losandes.persistence.entity.PhysicalMachine> physicalMachines);

    public java.lang.String startMonitorPhysicalMachines(ArrayList<PhysicalMachine> physicalMachines);

    public java.lang.String stopMonitorPhysicalMachines(ArrayList<PhysicalMachine> physicalMachines);

    public java.lang.String lockPhysicalMachine(java.util.ArrayList<com.losandes.persistence.entity.PhysicalMachine> physicalMachines);

    public java.lang.String unlockPhysicalMachine(java.util.ArrayList<com.losandes.persistence.entity.PhysicalMachine> physicalMachines);

    public java.lang.String updatePhysicalMachineAgent(java.util.ArrayList<com.losandes.persistence.entity.PhysicalMachine> physicalMachines);

    public com.losandes.persistence.entity.PhysicalMachine getPhysicalMachineByName(java.lang.String physicalMachineName);

    public java.util.List getAvailablePhysicalMachineCores(int templateSelected);

    public java.util.List getAvailablePhysicalMachineRAM(int templateSelected);

    public java.util.List getGridAvailablePhysicalMachineCores(int templateSelected, String systemUserName);

    public java.util.List getGridAvailablePhysicalMachineRAM(int templateSelected, String systemUserName);

    public java.util.List<ExecutionSlot> getExecutionSlots(String physicalMachineID);

    public void turnOnPhysicalMachine(PhysicalMachine bridge,PhysicalMachine ... toWake);

    public void sendMachineParameters(Map<String,String> map);

    public void updateAllPhysicalMachineAgents();

    public void setKeyPair(KeyPair keys);
}// end of IPhysicalMachineServices
