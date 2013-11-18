/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.virtualmachine;

import com.losandes.persistence.entity.*;
import javax.ejb.Local;

/**
 *
 * @author Clouder
 */
@Local
public interface VirtualMachineOperationsLocal {


    public VirtualMachineExecution[] generateVirtualExecutions(int vmCores, int vmRAM, int executionTime, SystemUser userName, VirtualMachine... cluster);
    public VirtualMachineExecution[] turnOnCluster(int vmCores, int vmRAM, int executionTime, SystemUser userName, VirtualMachine... cluster);
    public void sendFiles(boolean poweroff,int executionTime,PairMachineExecution ... cluster);
    public void configureVirtualMachine(PairMachineExecution mac,boolean poweroff,int executionTime);
    public void configureVirtualMachines(boolean poweroff,int executionTime, PairMachineExecution... virMacs);
    public void turnOnVirtualMachines(int executionTime,int cores,int ram, PairMachineExecution ... cluster);
    public void turnOnPhysicalMachines(int template, int executionTime, int numberInstances, int vmCores, int HDsize, int vmRAM, String userName);
    public void retrieveVirtualMachine(VirtualMachine toRetrieve,String user);
    public void writeFileOnVirtualMachine(String virtualMachineExecutionId,String path,String content);
}
