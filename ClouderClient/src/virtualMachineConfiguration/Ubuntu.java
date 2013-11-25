package virtualMachineConfiguration;

import utils.AddressUtility;
import virtualmachine.HypervisorOperationException;

import java.io.File;
import java.io.PrintWriter;

/**
 * @author Clouder
 */
public class Ubuntu extends AbstractVirtualMachineConfigurator{
    /**
     * Configures the ip address of a Ubuntu managed virtual machine
     * @param netmask
     * @param ip
     */
    @Override
    public void configureIP() throws HypervisorOperationException {
    	AddressUtility au = new AddressUtility(startMessage.getVmIP(),startMessage.getVirtualMachineNetMask());
    	File out=generateRandomFile();
    	try(PrintWriter pw = new LinuxPrintWriter(out)){
    		pw.println("auto lo");
            pw.println("iface lo inet loopback");
            pw.println("auto eth0");
            pw.println("iface eth0 inet static");
            pw.println("address " + au.getIp());
            pw.println("netmask " + au.getNetmask());
            pw.println("network " + au.getNetwork());
            pw.println("broadcast " + au.getBroadcast());
            pw.println("gateway " + au.getGateway());
    	}catch (Exception e) {
			return;
		}
    	copyFile("/etc/network/interfaces",out);
    	executeCommand("/bin/rm","/etc/udev/rules.d/*net.rules");
        executeCommand("/sbin/ifdown","eth0");
        executeCommand("/sbin/ifup","eth0");
        out.delete();
    }

    /**
     * Configures a DHCP client of the Ubuntu managed virtual machine
     */
    @Override
    public void configureDHCP() {
    }

	public void configureHostname() throws HypervisorOperationException{
		File out=generateRandomFile();
		try(PrintWriter pw = new LinuxPrintWriter(out)){
			pw.println(startMessage.getHostname());
        } catch (Exception e) {
            return;
        }
		copyFile("/etc/hostname",out);
		executeCommand("/bin/hostname",startMessage.getHostname());
	}

	@Override
	public void configureHostTable() throws HypervisorOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doPostConfigure() {
		// TODO Auto-generated method stub
		
	}

}