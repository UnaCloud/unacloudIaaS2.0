/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.virtualmachine;

import com.losandes.persistence.entity.*;

/**
 *
 * @author Clouder
 */
public class PairMachineExecution {

    private VirtualMachine virtualMachine;
    private VirtualMachineExecution execution;

    public PairMachineExecution(VirtualMachine virtualMachine, VirtualMachineExecution execution) {
        this.virtualMachine = virtualMachine;
        this.execution = execution;
    }

    public VirtualMachineExecution getExecution() {
        return execution;
    }

    public void setExecution(VirtualMachineExecution execution) {
        this.execution = execution;
    }

    public VirtualMachine getVirtualMachine() {
        return virtualMachine;
    }

    public void setVirtualMachine(VirtualMachine virtualMachine) {
        this.virtualMachine = virtualMachine;
    }
}
