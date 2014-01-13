package virtualMachineConfiguration;

import hypervisorManager.HypervisorOperationException;
import utils.AddressUtility;

/**
 *
 * @author Clouder
 */
public class Windows7 extends AbstractVirtualMachineConfigurator{

    @Override
    public void configureIP() throws HypervisorOperationException {
    	AddressUtility au = new AddressUtility(execution.getIp(),execution.getNetMask());
        executeCommand("netsh.exe","interface","ip","set","address","name=Conexión de Área local","static",au.getIp(),au.getNetmask(),au.getGateway(),"1");
    }
    @Override
    public void configureDHCP() {
    }
	@Override
	public void configureHostname() throws HypervisorOperationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void configureHostTable() throws HypervisorOperationException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean doPostConfigure() {
		return false;
	}
    
}