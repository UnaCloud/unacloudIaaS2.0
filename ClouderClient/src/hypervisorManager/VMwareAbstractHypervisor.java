/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hypervisorManager;

import static com.losandes.utils.Constants.ERROR_MESSAGE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import communication.messages.vmo.VirtualMachineStartMessage;
import virtualMachineManager.LocalProcessExecutor;
import virtualMachineManager.VirtualMachineExecution;
import virtualMachineManager.VirtualMachineImage;

/**
 * Implementation of hypervisor abstract class to give support for
 * VMwareWorkstation hypervisor.
 *
 * @author Clouder
 */

public abstract class VMwareAbstractHypervisor extends Hypervisor{
	public VMwareAbstractHypervisor(String path) {
		super(path);
	}
    @Override
    public void stopVirtualMachine(VirtualMachineImage image){
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T",getType(),"stop",image.getMainFile().getPath());
        /*if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }*/
    }

    @Override
    public void restartVirtualMachine(VirtualMachineImage image) throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T",getType(),"reset",image.getMainFile().getPath());
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }

    @Override
    public void preconfigureAndStartVirtualMachine(VirtualMachineExecution execution) throws HypervisorOperationException {
        if(execution.getCores()!=0&&execution.getMemory()!=0){
            Context vmx = new Context(execution.getImage().getMainFile().getPath());
            vmx.changeVMXFileContext(String.valueOf(execution.getCores()).toString(), String.valueOf(execution.getMemory()).toString(), execution.isPersistent());
        }
        correctDataStores();
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T",getType(),"start",execution.getImage().getMainFile().getPath(),"nogui");
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }
    
    @Override
    public void executeCommandOnMachine(VirtualMachineImage image,String command, String... args) throws HypervisorOperationException {
        List<String> com=new ArrayList<>();
        Collections.addAll(com, getExecutablePath(),"-T",getType(),"-gu",image.getUsername(),"-gp",image.getPassword(),"runProgramInGuest",image.getMainFile().getPath());
        com.add(command);
        Collections.addAll(com,args);
        String h = LocalProcessExecutor.executeCommandOutput(com.toArray(new String[0]));
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }

    @Override
    public void copyFileOnVirtualMachine(VirtualMachineImage image, String destinationRoute, File sourceFile) throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T",getType(),"-gu",image.getUsername(),"-gp",image.getPassword(),"copyFileFromHostToGuest",image.getMainFile().getPath(),sourceFile.getAbsolutePath(),destinationRoute);
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }

    @Override
    public void takeSnapshotOnMachine(VirtualMachineImage image,String snapshotname) throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T",getType(),"snapshot",image.getMainFile().getPath(),snapshotname);
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

    public void changeVirtualMachineMac(VirtualMachineImage image) throws HypervisorOperationException {
    }
	@Override
	public void registerVirtualMachine(VirtualMachineImage image) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void unregisterVirtualMachine(VirtualMachineImage image) {
		// TODO Auto-generated method stub
	}
	public abstract String getType();
}