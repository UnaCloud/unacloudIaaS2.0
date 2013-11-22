/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package machineConfigurators;

import com.losandes.utils.AddressUtility;

/**
 *
 * @author Clouder
 */
public class Windows7 extends AbstractSOConfigurator implements MachineConfigurator {

    @Override
    public void configureIP(String netmask, String ip) {
        AddressUtility au = new AddressUtility(ip, netmask);
        executeCommandInMachine("netsh.exe interface ip set address \"name=Conexión de área local\" static "+au.getIp()+" "+au.getNetmask()+" "+au.getGateway()+" 1");
    }
    @Override
    public void configureDHCP() {
    }

    @Override
    public void configureHostName() {
        //executeCommandInMachine("reg.exe add HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\ComputerName\\ComputerName /v ComputerName /t REG_SZ /d \""+virtualMachine.getVirtualMachineName()+"\" /f","reg.exe add HKEY_LOCAL_MACHINE\\SYSTEM\\ControlSet002\\services\\Tcpip\\Parameters /v \"NV Hostname\" /t REG_SZ /d \""+virtualMachine.getVirtualMachineName()+"\" /f","reg.exe add HKEY_LOCAL_MACHINE\\SYSTEM\\ControlSet002\\services\\Tcpip\\Parameters /v Hostname /t REG_SZ /d \""+virtualMachine.getVirtualMachineName()+"\" /f");
    }
    @Override
    public void configureHostTable() {
    }

    @Override
    public void doPostConfigure(boolean shutdown) {
        if(shutdown)stop();
    }
}