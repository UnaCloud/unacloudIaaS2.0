package com.losandes.virtualmachinesecurity;

import javax.ejb.Local;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Interface responsible for exposing the Virtual Machine Security services
 */
@Local
public interface IVirtualMachineSecurityServices {

    public boolean createVirtualMachineSecurity(com.losandes.persistence.entity.VirtualMachineSecurity virtualMachineSecurity);

    public boolean updateVirtualMachineSecurity(com.losandes.persistence.entity.VirtualMachineSecurity virtualMachineSecurity);

    public boolean deleteVirtualMachineSecurity(int code);

    public com.losandes.persistence.entity.VirtualMachineSecurity getVirtualMachineSecurityByID(int VirtualMachineSecurityID);

    public java.util.List getVirtualMachineSecurities();
    
}//end of IVirtualMachineSecurityServices
