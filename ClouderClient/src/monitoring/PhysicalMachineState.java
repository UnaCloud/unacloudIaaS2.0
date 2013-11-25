package monitoring;

import physicalmachine.Network;

import com.losandes.utils.Constants;

import communication.AbstractGrailsCommunicator;

/**
 * Responsible for reporting the physical machine state to Clouder Server
 */
public class PhysicalMachineState {
    public static void reportPhyisicalMachineState(int physicalMachineState){
    	AbstractGrailsCommunicator.pushInfo("machineState/reportPhysicalMachineState","hostname",Network.getHostname(),"operation",""+physicalMachineState);
    }
    public static void reportPhyisicalMachineLoggin(String user){
    	AbstractGrailsCommunicator.pushInfo("machineState/reportPhysicalMachineState","hostname",Network.getHostname(),"operation",""+Constants.LOGIN_DB,"hostuser",user);
    }
}
