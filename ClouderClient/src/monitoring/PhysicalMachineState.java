package monitoring;

import physicalmachine.Network;
import physicalmachine.OperatingSystem;

import com.losandes.utils.Constants;

import communication.AbstractGrailsCommunicator;

/**
 * Responsible for reporting the physical machine state to Clouder Server
 */
public class PhysicalMachineState {
	public static void reportPhyisicalMachineStart(){
		AbstractGrailsCommunicator.pushInfo("machineState/physicalMachineStart","hostname",Network.getHostname());
	}
	public static void reportPhyisicalMachineStop(){
		AbstractGrailsCommunicator.pushInfo("machineState/physicalMachineStop","hostname",Network.getHostname());
	}
	public static void reportPhyisicalMachineUserLogoff(){
		AbstractGrailsCommunicator.pushInfo("machineState/physicalMachineLogoff","hostname",Network.getHostname());
	}
	public static void reportPhyisicalMachineUserLogin(){
		AbstractGrailsCommunicator.pushInfo("machineState/reportPhysicalMachineLogin","hostname",Network.getHostname(),"hostuser",OperatingSystem.getUserName());
	}
	public static void registerPhysicalMachine(){
		AbstractGrailsCommunicator.pushInfo("machineState/registerPhysicalMachine","hostname",Network.getHostname(),"cores","1","ram","4096","mac","AA_BB_CC_DD_EE_AA","os","win7");
	}
}
