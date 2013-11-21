package virtualmachine;

import communication.messages.vmo.configuration.ExecuteCommandRequest;

import static com.losandes.utils.Constants.*;

import java.io.File;

import execution.LocalProcessExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of hypervisor abstract class to give support for VMwarePlayer
 * hypervisor.
 *
 * @author Clouder
 */
class VirtualBox extends Hypervisor {

    @Override
    protected void setExecutablePath(String executablePath) {
        super.setExecutablePath("C:\\Program Files\\Oracle\\VirtualBox\\VBoxManage.exe");
    }
    
    @Override
    public void turnOffVirtualMachine() throws HypervisorOperationException {
        sleep(2000);
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "controlvm", getVirtualMachineName(), "poweroff");
        unregisterVirtualMachine();
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }
    private void registerVirtualMachine(){
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "registervm", getVirtualMachinePath());
        sleep(15000);
    }
    private void unregisterVirtualMachine(){
        sleep(15000);
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "unregistervm", getVirtualMachineName());
    }
    @Override
    public void restartVirtualMachine() throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "controlvm", getVirtualMachineName(), "reset");
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }

    @Override
    protected Hypervisor getInstance() {
        return new VirtualBox();
    }

    @Override
    public void preconfigureAndStartVirtualMachine(int coreNumber, int ramSize, String persistent) throws HypervisorOperationException {
        registerVirtualMachine();
        if (hasSnapshot()) {
            LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "snapshot", getVirtualMachineName(), "restorecurrent");
            sleep(10000);
        }
        if(coreNumber!=0&&ramSize!=0){
            LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "modifyvm", getVirtualMachineName(),"--memory",""+ramSize,"--cpus",""+coreNumber);
            sleep(10000);
        }
        String h;
        if ((h=LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "startvm", getVirtualMachineName(), "--type", "headless")).contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
        sleep(30000);
        LocalProcessExecutor.executeCommandOutput("wmic","process","where","name=\"VBoxHeadless.exe\"","CALL","setpriority","64");
    }
    

    @Override
    public void executeCommandOnMachine(String user, String pass, ExecuteCommandRequest command) throws HypervisorOperationException {
        pass = pass.replace(" ", "").replace("\"", "");
        user = user.replace(" ", "").replace("\"", "");
        List<String> com = new ArrayList<>();
        Collections.addAll(com, getExecutablePath(), "--nologo", "guestcontrol", getVirtualMachineName(), "execute", "--image", command.getExecName(), "--username", user, "--password", pass, "--wait-exit", "--");
        Collections.addAll(com, command.getVars());
        String h = LocalProcessExecutor.executeCommandOutput(com.toArray(new String[0]));
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
        sleep(10000);
    }

    @Override
    public void copyFileOnVirtualMachine(String user, String pass, String destinationRoute, File sourceFile) throws HypervisorOperationException {
        pass = pass.replace(" ", "").replace("\"", "");
        user = user.replace(" ", "").replace("\"", "");
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "guestcontrol", getVirtualMachineName(), "copyto", sourceFile.getAbsolutePath(), destinationRoute, "--username", user, "--password", pass);
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
        sleep(10000);
    }

    @Override
    public void takeSnapshotOnMachine(String snapshotname) throws HypervisorOperationException {
        registerVirtualMachine();
        String h=LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"snapshot",getVirtualMachineName(),"take",snapshotname);
        unregisterVirtualMachine();
    }

    @Override
    public void changeVirtualMachineMac() throws HypervisorOperationException {
        registerVirtualMachine();
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "modifyvm", getVirtualMachineName(),"--macaddress1","auto");
        unregisterVirtualMachine();
    }

    
    private boolean hasSnapshot() {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(), "snapshot", getVirtualMachineName(), "list");
        return h != null && !h.contains("does not");
    }

    @Override
    public int getHypervisorId() {
        return VIRTUAL_BOX;
    }

    private void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException ex) {
            Logger.getLogger(VirtualBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}