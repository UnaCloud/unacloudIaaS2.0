/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualmachine;

import execution.LocalProcessExecutor;
import static com.losandes.utils.Constants.*;
import com.losandes.utils.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import physicalmachine.Network;
import physicalmachine.PhysicalMachine;

/**
 * Implementation of hypervisor abstract class to give support for
 * VMwareWorkstation hypervisor.
 *
 * @author Clouder
 */
public class VMwareWorkstation extends Hypervisor {

    @Override
    protected void setVirtualMachinePath(String virtualMachinePath) {
        virtualMachinePath = virtualMachinePath.replace("\"", "");
        if (!virtualMachinePath.contains(VMW_VMX_EXTENSION)) {
            virtualMachinePath += VMW_VMX_EXTENSION;
        }
        super.setVirtualMachinePath(virtualMachinePath);
    }

    @Override
    protected void setExecutablePath(String executablePath) {
        executablePath = executablePath.replace("\"", "");
        if (!executablePath.endsWith("vmrun.exe")) {
            if (!executablePath.endsWith("\\") && !executablePath.endsWith("/")) {
                executablePath += "\\vmrun.exe";
            } else {
                executablePath += "vmrun.exe";
            }
        }
        super.setExecutablePath(executablePath);
    }

    @Override
    public void turnOnVirtualMachine() throws HypervisorOperationException {
        correctDataStores();
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T","ws","start",getVirtualMachinePath(),"nogui");
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }

    @Override
    public void turnOffVirtualMachine() throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T","ws","stop",getVirtualMachinePath());
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }

    @Override
    public void restartVirtualMachine() throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T","ws","reset",getVirtualMachinePath());
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }

    @Override
    protected Hypervisor getInstance() {
        return new VMwareWorkstation();
    }

    @Override
    public void preconfigureVirtualMachine(int coreNumber, int ramSize, String persistant) throws HypervisorOperationException {
        Context vmx = new Context(getVirtualMachinePath());
        vmx.changeVMXFileContext(String.valueOf(coreNumber).toString(), String.valueOf(ramSize).toString(), persistant != null && persistant.equals("true"));
    }

    @Override
    public void executeCommandOnMachine(String user, String pass, String command) throws HypervisorOperationException {
        pass = pass.replace(" ", "").replace("\"", "");
        user = user.replace(" ", "").replace("\"", "");
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T","ws","-gu",user,"-gp",pass,"runProgramInGuest",getVirtualMachinePath(),command);
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }

    @Override
    public void copyFileOnVirtualMachine(String user, String pass, String destinationRoute, File sourceFile) throws HypervisorOperationException {
        pass = pass.replace(" ", "").replace("\"", "");
        user = user.replace(" ", "").replace("\"", "");
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T","ws","-gu",user,"-gp",pass,"copyFileFromHostToGuest",getVirtualMachinePath(),sourceFile.getAbsolutePath(),destinationRoute);
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }

    @Override
    public void takeSnapshotOnMachine(String snapshotname) throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T","ws","snapshot",getVirtualMachinePath(),snapshotname);
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }

    private void correctDataStores() {
        try {
            FileInputStream fis = new FileInputStream("./datastores.xml");
            FileOutputStream fos = new FileOutputStream("C:\\ProgramData\\VMware\\hostd\\datastores.xml");
            byte[] b = new byte[1024];
            for (int n; (n = fis.read(b)) != -1;) {
                fos.write(b, 0, n);
            }
            fis.close();
            fos.close();
        } catch (Throwable th) {
        }
    }

    public static void main(String... args) throws Exception {
    }

    public static void startUpServices() {
        Log.print2("startUpServices");
        TreeSet<String> serviciosALevantar = new TreeSet<String>();
        serviciosALevantar.add("VMware Authorization Service");
        serviciosALevantar.add("hcmon");
        serviciosALevantar.add("VMware NAT Service");
        serviciosALevantar.add("VMware Workstation Server");
        String servicios = "";
        try {
            PrintWriter pw = new PrintWriter("./logServicios.txt");
            pw.println(new Date() + " " + System.currentTimeMillis());
            Process p = Runtime.getRuntime().exec("net start");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            for (String h; (h = br.readLine()) != null;) {
                serviciosALevantar.remove(h.trim());
                if (h.toLowerCase().contains("vmware")) {
                    servicios += h + "\r\n";
                }
            }
            br.close();

            pw.println("levantando " + serviciosALevantar.size());
            if (!serviciosALevantar.isEmpty()) {
                try {
                    for (String servs : serviciosALevantar) {
                        pw.println("net start \"" + servs + "\"");
                        p = Runtime.getRuntime().exec("net start \"" + servs + "\"");
                        br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        for (String h; (h = br.readLine()) != null;) {
                            pw.println(h);
                        }
                        br.close();
                        br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        for (String h; (h = br.readLine()) != null;) {
                            pw.println(h);
                        }
                        br.close();
                        p.waitFor();
                    }
                    pw.println("termine bien");
                } catch (Exception ex) {
                    ex.printStackTrace(pw);
                }
            }
            pw.close();
        } catch (Exception e) {
        }
        Connection con=null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://157.253.236.160:3306/clouder", "temporal", "q3hju3qhy3y4dfg62");
            PreparedStatement ps = con.prepareStatement("update `clouder`.`nodestatelog` set `CLIENTSTATE`=?,`SERVICESNOTRUNNING`=? where `PHYSICALMACHINE_PHYSICALMACHINENAME`=?;");
            ps.setString(1,"Ok");
            ps.setString(2,servicios);
            ps.setString(3,Network.getHostname());
            ps.executeUpdate();
            con.close();
        }catch(SQLException ex){
        }finally{
            if(con!=null)try {
                con.close();
            } catch (SQLException ex) {
            }
        }
    }
}