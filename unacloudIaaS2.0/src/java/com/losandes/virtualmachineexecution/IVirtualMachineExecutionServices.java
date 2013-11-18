package com.losandes.virtualmachineexecution;

import javax.ejb.Local;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Interface responsible for exposing the Virtual Machine Execution services
 */
@Local
public interface IVirtualMachineExecutionServices {

    public boolean createVirtualMachineExecution(com.losandes.persistence.entity.VirtualMachineExecution virtualMachineExecution);

    public boolean updateVirtualMachineExecution(com.losandes.persistence.entity.VirtualMachineExecution virtualMachineExecution);

    public boolean deleteVirtualMachineExecution(String code);

    public com.losandes.persistence.entity.VirtualMachineExecution getVirtualMachineExecutionByID(String VirtualMachineExecutionID);

    public java.util.List getVirtualMachineExecutions();

}// end of IVirtualMachineExecutionServices
