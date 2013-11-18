/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.communication.messages.configuration;

import com.losandes.communication.messages.UnaCloudMessage;
import static com.losandes.communication.messages.configuration.ConfigurationAbstractMessage.VMC_STOP;

/**
 *
 * @author Clouder
 */
public class StopVirtualMachineMessage extends ConfigurationAbstractMessage{
    String hypervisor, destinationMachine, rutaVMRUN;

    public StopVirtualMachineMessage(String hypervisor, String destinationMachine, String rutaVMRUN) {
        super(VMC_STOP);
        this.hypervisor = hypervisor;
        this.destinationMachine = destinationMachine;
        this.rutaVMRUN = rutaVMRUN;
    }
        
    public StopVirtualMachineMessage(UnaCloudMessage message){
        super(VMC_STOP,message);
    }

    public String getHypervisor() {
        return hypervisor;
    }

    public String getDestinationMachine() {
        return destinationMachine;
    }

    public String getRutaVMRUN() {
        return rutaVMRUN;
    }
    
    @Override
    public void processMessage(UnaCloudMessage message){
        hypervisor = message.getString(2);
        destinationMachine = message.getString(3);
        rutaVMRUN = message.getString(4);
    }
}