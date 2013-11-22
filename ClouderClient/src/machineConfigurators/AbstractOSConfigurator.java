/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package machineConfigurators;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.security.utils.*;
import com.losandes.communication.security.SecureSocket;
import com.losandes.deploy.IPGenerationPolicy;
import com.losandes.multicast.UnicastSender;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.virtualmachine.PairMachineExecution;
import com.losandes.virtualmachineexecution.IVirtualMachineExecutionServices;
import com.losandes.vo.HostTable;
import java.io.File;
import static com.losandes.utils.Constants.*;
import static com.losandes.communication.messages.configuration.ConfigurationAbstractMessage.*;
import com.losandes.deploy.MACGenerationPolicy;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract operating system configurator that implements a set ot utility methods to be used by virtual machine configurations
 * @author Clouder
 */
public abstract class AbstractOSConfigurator {

	public abstract void startVirtualMachine();
	public abstract void stopVirtualMachine();
	public abstract void writeMachineFile(String path,byte[] content);
	public abstract void changeMac();
    /**
     * Starts the managed virtual machine
     */
    public void start() {
        try {
            SecureSocket socket = new SecureSocket(virtualMachine.getPhysicalMachine().getPhysicalMachineIP(),clouderClientPort);
            AbstractCommunicator communication = socket.connect();
            communication.writeUTF("" + UnaCloudAbstractMessage.VIRTUAL_MACHINE_CONFIGURATION,VMC_START, virtualMachine.getHypervisor().getHypervisorCode().intValue()+"", virtualMachine.getVirtualMachinePath(), virtualMachine.getPhysicalMachine().getPhysicalMachineHypervisorPath());
            communication.readUTF();
            communication.close();
            Thread.sleep(30000);
        } catch (Exception ex) {
            Logger.getLogger(AbstractSOConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Stops the managed virtual machine
     */
    public void stop() {
        try {
            SecureSocket socket = new SecureSocket(virtualMachine.getPhysicalMachine().getPhysicalMachineIP(),clouderClientPort);
            AbstractCommunicator communication = socket.connect();
            communication.writeUTF(new String[]{"" + UnaCloudAbstractMessage.VIRTUAL_MACHINE_CONFIGURATION,VMC_STOP, virtualMachine.getHypervisor().getHypervisorCode().intValue()+"",virtualMachine.getVirtualMachinePath(), virtualMachine.getPhysicalMachine().getPhysicalMachineHypervisorPath()});
            communication.readUTF();
            communication.close();
        } catch (ConnectionException ex) {
            Logger.getLogger(AbstractSOConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Execute a list of commands on the managed virtual machine
     * @param comandos
     */
    public void executeCommandInMachine(String... comandos) {
        try {
            SecureSocket socket = new SecureSocket(virtualMachine.getPhysicalMachine().getPhysicalMachineIP(),clouderClientPort);
            AbstractCommunicator communication = socket.connect();
            String[] peticion = new String[8 + comandos.length];
            peticion[0] = "" + UnaCloudAbstractMessage.VIRTUAL_MACHINE_CONFIGURATION;
            peticion[1] = VMC_COMMAND;
            peticion[2] = virtualMachine.getHypervisor().getHypervisorCode().intValue()+"";
            peticion[3] = virtualMachine.getVirtualMachinePath();
            peticion[4] = virtualMachine.getVirtualMachineSecurity().getVirtualMachineSecurityUser();
            peticion[5] = virtualMachine.getVirtualMachineSecurity().getVirtualMachineSecurityPassword();
            peticion[6] = virtualMachine.getPhysicalMachine().getPhysicalMachineHypervisorPath();
            peticion[7] = "" + comandos.length;
            for (int e = 0; e < comandos.length; e++) {
                peticion[8 + e] = comandos[e];
            }
            communication.writeUTF(peticion);
            communication.readUTF();
            communication.close();
        } catch (ConnectionException ex) {
            Logger.getLogger(AbstractSOConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void takeSnapshotOnMachine(String snapshotname) {
        try {
            SecureSocket socket = new SecureSocket(virtualMachine.getPhysicalMachine().getPhysicalMachineIP(),clouderClientPort);
            AbstractCommunicator communication = socket.connect();
            String[] peticion = new String[6];
            peticion[0] = "" + UnaCloudAbstractMessage.VIRTUAL_MACHINE_CONFIGURATION;
            peticion[1] = VMC_TAKE_SNAPSHOT;
            peticion[2] = virtualMachine.getHypervisor().getHypervisorCode().intValue()+"";
            peticion[3] = virtualMachine.getVirtualMachinePath();
            peticion[4] = virtualMachine.getPhysicalMachine().getPhysicalMachineHypervisorPath();
            peticion[5] = snapshotname;
            communication.writeUTF(peticion);
            communication.readUTF();
            communication.close();
        } catch (ConnectionException ex) {
            Logger.getLogger(AbstractSOConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is responsible to configure the ip of the managed virtual machine
     * @param netmask
     * @param ip
     */
    public abstract void configureIP(String netmask, String ip);

    /**
     * This method is responsible to configure a DHCP client on the managed virtual machine
     */
    public abstract void configureDHCP();

    /**
     * This method is responsible to configure the host name of the managed virtual machine
     */
    public abstract void configureHostName();

    /**
     * This method is responsible to configure the host table of the managed virtual machine
     */
    public abstract void configureHostTable();

    /**
     * Configures this Debian managed virtual machine
     * @param virtualMachineExecution
     * @param hosts
     * @param shutdown
     */
    public final void configureMachine(String virtualMachineExecutionId,String ip){
        if(virtualMachine.getMacPolicy()== MACGenerationPolicy.RANDOM)changeMac();
        else if(virtualMachine.getMacPolicy()==MACGenerationPolicy.STATIC_MACHINE_BASED);
        System.out.println("Start");
        start();
        System.out.println("Host name");
        virtualMachineExecution.getExecution().setVirtualMachineExecutionStatusMessage("Setting hostname");
        virtualMachineExecution.getExecution().setCurrent(2);
        if(virtualMachineExecutionServices!=null)virtualMachineExecutionServices.updateVirtualMachineExecution(virtualMachineExecution.getExecution());
        configureHostName();
        virtualMachineExecution.getExecution().setVirtualMachineExecutionStatusMessage("Setting IP");
        virtualMachineExecution.getExecution().setCurrent(3);
        if(virtualMachineExecutionServices!=null)virtualMachineExecutionServices.updateVirtualMachineExecution(virtualMachineExecution.getExecution());
        System.out.println(vm.getIpPolicy());
        if (vm.getIpPolicy() == IPGenerationPolicy.PUBLIC_MACHINE_BASED) {
            configureIP(vm.getPhysicalMachine().getPhysicalMachineVirtualNetmask(), vm.getVirtualMachineIP());
        } else if (vm.getIpPolicy() == IPGenerationPolicy.DHCP) {
            configureDHCP();//Completar
        } else if (vm.getIpPolicy() == IPGenerationPolicy.PRIVATE) {
            configureIP(vm.getPhysicalMachine().getPhysicalMachineVirtualNetmask(), vm.getVirtualMachineIP());//Completar
        }
        if(!vm.isPersistent()){
            stop();
            waitTime(10000);
            takeSnapshotOnMachine("base");
        }else doPostConfigure(shutdown);
        /*System.out.println(virtualMachine.getTemplate().getTemplateType().getTemplateTypeName());
        if(virtualMachine.getTemplate().getTemplateType().getTemplateTypeName().toLowerCase().equals("grid")){
        configureHostTable();
        //Configurar enlaces lógicos
        }*/
        
    }
    public abstract void doPostConfigure(boolean shutdown);
    public final void waitTime(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(AbstractSOConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
