/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.communication.messages.configuration;

import com.losandes.communication.messages.UnaCloudMessage;
import static com.losandes.communication.messages.configuration.ConfigurationAbstractMessage.VMC_WRITE_FILE;

/**
 *
 * @author Clouder
 */
public class WriteFileOnVirtualMachineMessage extends ConfigurationAbstractMessage{
    String hypervisor,destinationMachine, login, pass, destinationFileRute, rutaVMRUN;

    public WriteFileOnVirtualMachineMessage(String hypervisor, String destinationMachine, String login, String pass, String destinationFileRute, String rutaVMRUN) {
        super(VMC_WRITE_FILE);
        this.hypervisor = hypervisor;
        this.destinationMachine = destinationMachine;
        this.login = login;
        this.pass = pass;
        this.destinationFileRute = destinationFileRute;
        this.rutaVMRUN = rutaVMRUN;
    }

    public WriteFileOnVirtualMachineMessage(UnaCloudMessage message) {
        super(VMC_WRITE_FILE, message);
    }

    public String getHypervisor() {
        return hypervisor;
    }

    public String getDestinationMachine() {
        return destinationMachine;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getDestinationFileRute() {
        return destinationFileRute;
    }

    public String getRutaVMRUN() {
        return rutaVMRUN;
    }

    @Override
    protected void processMessage(UnaCloudMessage message) {
        hypervisor = message.getString(2);
        destinationMachine = message.getString(3);
        login = message.getString(4);
        pass = message.getString(5);
        destinationFileRute = message.getString(6);
        rutaVMRUN = message.getString(7);
    }
    
}
