
package unacloudws.responses;

import java.util.Date;


public class VirtualMachineExecutionWS {
	
	protected String id;
	protected String imageName;
    protected String virtualMachineExecutionIP;
    protected String virtualMachineExecutionStatus;
    protected String virtualMachineExecutionStatusMessage;
    protected Date stopTime;
    protected String virtualMachineName;
	
    
    public VirtualMachineExecutionWS(String imageName,
			String virtualMachineExecutionIP,
			String virtualMachineExecutionStatus,
			String virtualMachineExecutionStatusMessage, String id, Date stopTime,
			String virtualMachineName) {
		this.imageName = imageName;
		this.virtualMachineExecutionIP = virtualMachineExecutionIP;
		this.virtualMachineExecutionStatus = virtualMachineExecutionStatus;
		this.virtualMachineExecutionStatusMessage = virtualMachineExecutionStatusMessage;
		this.stopTime = stopTime;
		this.id= id;
		this.virtualMachineName = virtualMachineName;
	}
    
    

}
