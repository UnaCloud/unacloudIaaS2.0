/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.machineconfigurators;

import com.losandes.utils.AddressUtility;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * Class responsible to implement methods to configure Debian virtual machines
 * @author Clouder
 */
public class Debian extends AbstractSOConfigurator implements MachineConfigurator {

    /**
     * Configures the ip address of the Debian managed virtual machine
     * @param netmask
     * @param ip
     */
    @Override
    public void configureIP(String netmask, String ip) {
        AddressUtility au = new AddressUtility(ip, netmask);
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        PrintWriter pw = new LinuxPrintWriter(bos);
        pw.println("auto lo");
        pw.println("iface lo inet loopback");
        pw.println("auto eth0");
        pw.println("iface eth0 inet static");
        pw.println("address " + au.getIp());
        pw.println("netmask " + au.getNetmask());
        pw.println("network " + au.getNetwork());
        pw.println("broadcast " + au.getBroadcast());
        pw.println("gateway " + au.getGateway());
        pw.close();
        writeMachineFile("/etc/network/interfaces",bos.toByteArray());
        executeCommandInMachine("/etc/init.d/networking restart");
    }

    /**
     * Configures a DHCP client of the Debian managed virtual machine
     */
    @Override
    public void configureDHCP() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Configures the host name of the Debian managed virtual machine
     */
    @Override
    public void configureHostName() {
        try {
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            PrintWriter pw = new LinuxPrintWriter(bos);
            pw.println("" + virtualMachine.getVirtualMachineName());
            pw.close();
            writeMachineFile("/etc/hostname",bos.toByteArray());
            executeCommandInMachine("/bin/hostname " + virtualMachine.getVirtualMachineName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Configure the host table of the Debian managed virtual machine
     */
    @Override
    public void configureHostTable() {
        try {
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            PrintWriter pw = new LinuxPrintWriter(bos);
            pw.println("127.0.0.1 localhost");
            for (int e = 0; e < hosts.size(); e++)pw.println(hosts.get(e).ip + " " + hosts.get(e).hostName);
            writeMachineFile("/etc/hosts",bos.toByteArray());
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPostConfigure(boolean shutdown){
        if (shutdown)stop();
    }
}
