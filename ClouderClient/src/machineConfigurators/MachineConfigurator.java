/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machineConfigurators;

import com.losandes.virtualmachine.PairMachineExecution;
import com.losandes.virtualmachineexecution.IVirtualMachineExecutionServices;
import com.losandes.vo.HostTable;

/**
 * Interface that defines the methods that must be implemented by a virtual machine configurator
 * @author Clouder
 */
public interface MachineConfigurator {

    /**
     * Inits the configurator to used the given communication and file transfer ports
     * @param clouderClientPort
     * @param fileTransferSocket
     */
    public void init(int clouderClientPort,int fileTransferSocket);

    /**
     * Configures the given virtual machine
     * @param virtualMachine
     * @param hosts
     * @param shutdown
     */
    public void configureMachine(PairMachineExecution virtualMachine, HostTable hosts,boolean shutdown);

    /**
     * Utility method.
     * @param vmes
     */
    public void setVirtualMachineExecutionServices(IVirtualMachineExecutionServices vmes);
}
