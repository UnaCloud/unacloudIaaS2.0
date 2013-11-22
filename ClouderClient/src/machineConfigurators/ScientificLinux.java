/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package machineConfigurators;

import com.losandes.utils.AddressUtility;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Class responsible to implement methods to configure Scientific Linux virtual machines
 * @author Clouder
 */
public class ScientificLinux extends AbstractSOConfigurator implements MachineConfigurator {

    /**
     * Configures the ip address of the Scientific Linux managed virtual machine
     * @param netmask
     * @param ip
     */
    @Override
    public void configureIP(String netmask, String ip) {
        AddressUtility au = new AddressUtility(ip, netmask);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        PrintWriter pw = new LinuxPrintWriter(bos);
        pw.println("# Advanced Micro Devices [AMD] 79c970 [PCnet32 LANCE]");
        pw.println("DEVICE=eth0");
        pw.println("BOOTPROTO=none");
        pw.println("ONBOOT=yes");
        pw.println("NETMASK=" + au.getNetmask());
        pw.println("IPADDR=" + au.getIp());
        pw.println("GATEWAY=" + au.getGateway());
        pw.println("TYPE=Ethernet");
        pw.println("USERCTL=no");
        pw.println("IPV6INIT=no");
        pw.println("PEERDNS=yes");
        pw.close();
        writeMachineFile("/etc/sysconfig/networking/profiles/default/ifcfg-eth0",bos.toByteArray());
        executeCommandInMachine("/sbin/ifdown eth0", "/sbin/ifup eth0");
    }

    /**
     * Configures a DHCP client of the Scientific Linux managed virtual machine
     */
    @Override
    public void configureDHCP() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Configures the host name of the Scientific Linux managed virtual machine
     */
    @Override
    public void configureHostName() {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        PrintWriter pw = new LinuxPrintWriter(bos);
        pw.println("NETWORKING=yes");
        pw.println("NETWORKING_IPV6=no");
        pw.println("HOSTNAME=" + virtualMachine.getVirtualMachineName());
        pw.close();
        writeMachineFile("/etc/sysconfig/network",bos.toByteArray());
        executeCommandInMachine("/bin/hostname " + virtualMachine.getVirtualMachineName());
    }

    /**
     * Configure the host table of the Scientific Linux managed virtual machine
     */
    @Override
    public void configureHostTable() {
        try {
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            PrintWriter pw = new LinuxPrintWriter(bos);
            pw.println("127.0.0.1 localhost");
            for (int e = 0; e < hosts.size(); e++) {
                pw.println(hosts.get(e).ip + " " + hosts.get(e).hostName);
            }
            writeMachineFile("/etc/hosts",bos.toByteArray());
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void doPostConfigure(boolean shutdown) {
        if(shutdown)stop();
    }
}