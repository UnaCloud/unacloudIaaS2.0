package utils;

import physicalmachine.Network;
import communication.AbstractGrailsCommunicator;

public class UnaCloudLogger {

	public static void log(String component,String message){
		AbstractGrailsCommunicator.pushInfo("UnaCloudServices/clouderClientAttention","hostname",Network.getHostname(),"component",component,message,message);
	}
}
