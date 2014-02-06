
package unacloudws.responses;

import java.util.Date;


public class VirtualMachineExecutionWS {
	
	protected int id;
	protected String imageName;
    protected String virtualMachineExecutionIP;
    protected VirtualMachineStatusEnum virtualMachineExecutionStatus;
    protected String virtualMachineExecutionStatusMessage;
    protected Date stopTime;
    protected String virtualMachineName;
	
    
    public VirtualMachineExecutionWS(String imageName,
			String virtualMachineExecutionIP,
			VirtualMachineStatusEnum virtualMachineExecutionStatus,
			String virtualMachineExecutionStatusMessage, int id, Date stopTime,
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
