package com.losandes.virtualmachineexecution;

import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.VirtualMachineExecution;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for exposing the VirtualMachineExecution persistence services
 */
@Stateless
public class VirtualMachineExecutionServices implements IVirtualMachineExecutionServices {

    @EJB
    private IPersistenceServices persistenceServices;

    /**
     * Responsible for exposing the Virtual Machine Execution create persistence services
     */
    public boolean createVirtualMachineExecution(VirtualMachineExecution virtualMachineExecution) {
        return persistenceServices.create(virtualMachineExecution);
    }

    /**
     * Responsible for exposing the Virtual Machine Execution update persistence services
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean updateVirtualMachineExecution(VirtualMachineExecution virtualMachineExecution) {
        return persistenceServices.update(virtualMachineExecution)!=null;
    }

    /**
     * Responsible for exposing the Virtual Machine Execution delete persistence services
     */
    public boolean deleteVirtualMachineExecution(String code) {
        VirtualMachineExecution virtualMachineExecution = getVirtualMachineExecutionByID(code);
        return persistenceServices.delete(virtualMachineExecution);
    }

    public VirtualMachineExecution getVirtualMachineExecutionByID(String VirtualMachineExecutionID) {
        return (VirtualMachineExecution) persistenceServices.findById(VirtualMachineExecution.class, VirtualMachineExecutionID);
    }

    /**
     * Responsible for exposing all the Virtual Machine Executions available
     */
    public List getVirtualMachineExecutions() {
        return persistenceServices.findAll(VirtualMachineExecution.class);
    }

}// end of VirtualMachineExecution

