/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.multicast;

import com.losandes.persistence.entity.VirtualMachineExecution;
import javax.ejb.Local;

/**
 *
 * @author Clouder
 */
@Local
public interface VirtualMachineRetriverLocal {

    /**
     * Retrieves a virtual machine to store it as a new deplayable template
     * @param ip IP address of the host with the virtual machine
     * @param remotePath The path which contains the virtual machine on the remote host
     * @param localPath The path which will be used to store the retrieved virtual machine
     * @param vme The virtual machine execution that represents this virtual machine operation
     */
    public void retrieveVirtualMachine(String ip, String remotePath,String localPath,VirtualMachineExecution vme);

    /**
     * Update a virtual machine execution with the given message and step
     * @param vme
     * @param message
     * @param current
     */
    public void refreshExecutions(VirtualMachineExecution vme, String message, int current);
}
