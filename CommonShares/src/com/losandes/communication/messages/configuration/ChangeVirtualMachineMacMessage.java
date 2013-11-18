/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.communication.messages.configuration;

import com.losandes.communication.messages.UnaCloudMessage;
import static com.losandes.communication.messages.configuration.ConfigurationAbstractMessage.VMC_CHANGE_MAC;
import static com.losandes.communication.messages.configuration.ConfigurationAbstractMessage.VMC_START;

/**
 *
 * @author Clouder
 */
public class ChangeVirtualMachineMacMessage extends ConfigurationAbstractMessage{
    String hypervisor, destinationMachine, rutaVMRUN;

    public ChangeVirtualMachineMacMessage(String hypervisor, String destinationMachine, String rutaVMRUN) {
        super(VMC_CHANGE_MAC);
        this.hypervisor = hypervisor;
        this.destinationMachine = destinationMachine;
        this.rutaVMRUN = rutaVMRUN;
    }
        
    public ChangeVirtualMachineMacMessage(UnaCloudMessage message){
        super(VMC_CHANGE_MAC,message);
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