/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualmachine;

import static com.losandes.utils.Constants.ERROR_MESSAGE;
import static com.losandes.utils.Constants.VMW;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import execution.LocalProcessExecutor;

/**
 * Implementation of hypervisor abstract class to give support for
 * VMwareWorkstation hypervisor.
 *
 * @author Clouder
 */

public class VMwareWorkstation extends Hypervisor {

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
    public void stopVirtualMachine(){
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T","ws","stop",getVirtualMachinePath());
        /*if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }*/
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
    public void preconfigureAndStartVirtualMachine(int coreNumber, int ramSize, String persistant) throws HypervisorOperationException {
        if(coreNumber!=0&&ramSize!=0){
            Context vmx = new Context(getVirtualMachinePath());
            vmx.changeVMXFileContext(String.valueOf(coreNumber).toString(), String.valueOf(ramSize).toString(), persistant != null && persistant.equals("true"));
        }
        correctDataStores();
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T","ws","start",getVirtualMachinePath(),"nogui");
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }
    
    @Override
    public void executeCommandOnMachine(String user, String pass,String command, String... args) throws HypervisorOperationException {
    	pass = pass.replace(" ", "").replace("\"", "");
        user = user.replace(" ", "").replace("\"", "");
        List<String> com=new ArrayList<>();
        Collections.addAll(com, getExecutablePath(),"-T","ws","-gu",user,"-gp",pass,"runProgramInGuest",getVirtualMachinePath());
        com.add(command);
        Collections.addAll(com,args);
        String h = LocalProcessExecutor.executeCommandOutput(com.toArray(new String[0]));
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

    @Override
    public int getHypervisorId() {
        return VMW;
    }
    public void changeVirtualMachineMac() throws HypervisorOperationException {
    }
}