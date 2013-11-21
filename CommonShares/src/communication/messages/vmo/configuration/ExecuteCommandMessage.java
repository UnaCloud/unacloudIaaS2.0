/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication.messages.vmo.configuration;

import communication.UnaCloudMessage;
import static communication.messages.vmo.configuration.ConfigurationAbstractMessage.VMC_COMMAND;

import java.util.Arrays;

/**
 *
 * @author Clouder
 */
public class ExecuteCommandMessage extends ConfigurationAbstractMessage{
    int hypervisor;
    String destinationMachine,user,pass,rutaVMRUN;
    ExecuteCommandRequest[] comandos;
    public ExecuteCommandMessage(int hypervisor, String destinationMachine, String user, String pass, String rutaVMRUN, String configurationOperation) {
        super(VMC_COMMAND);
        this.hypervisor = hypervisor;
        this.destinationMachine = destinationMachine;
        this.user = user;
        this.pass = pass;
        this.rutaVMRUN = rutaVMRUN;
    }
    public ExecuteCommandMessage(UnaCloudMessage message){
        super(VMC_COMMAND,message);
    }
    
    public int getHypervisor() {
        return hypervisor;
    }

    public String getDestinationMachine() {
        return destinationMachine;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getRutaVMRUN() {
        return rutaVMRUN;
    }

    public ExecuteCommandRequest[] getComandos() {
        return comandos;
    }
    
    @Override
    public void processMessage(UnaCloudMessage message){
        hypervisor = message.getInteger(2);
        destinationMachine = message.getString(3);
        user = message.getString(4);
        pass = message.getString(5);
        rutaVMRUN = message.getString(6);
        comandos=new ExecuteCommandRequest[message.getInteger(7)];
        for (int e = 0; e < comandos.length; e++){
            String command[]=message.getString(8 + e).split(" +");
            comandos[e]=new ExecuteCommandRequest(command[0],Arrays.copyOfRange(command,1,command.length));
        }
    }
}