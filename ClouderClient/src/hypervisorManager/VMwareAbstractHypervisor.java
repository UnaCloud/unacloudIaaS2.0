package hypervisorManager;

import static com.losandes.utils.Constants.ERROR_MESSAGE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import virtualMachineManager.LocalProcessExecutor;
import virtualMachineManager.VirtualMachineImage;

/**
 * Implementation of hypervisor abstract class to give support for
 * VMwareWorkstation hypervisor.
 */

public abstract class VMwareAbstractHypervisor extends Hypervisor{
	public VMwareAbstractHypervisor(String path) {
		super(path);
	}
    @Override
    public void stopVirtualMachine(VirtualMachineImage image){
        LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T",getType(),"stop",image.getMainFile().getPath());
    }

    @Override
    public void restartVirtualMachine(VirtualMachineImage image) throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T",getType(),"reset",image.getMainFile().getPath());
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }

    @Override
    public void startVirtualMachine(VirtualMachineImage image) throws HypervisorOperationException {
        correctDataStores();
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T",getType(),"start",image.getMainFile().getPath(),"nogui");
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
    public void takeVirtualMachineSnapshot(VirtualMachineImage image,String snapshotname) throws HypervisorOperationException {
        String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T",getType(),"snapshot",image.getMainFile().getPath(),snapshotname);
        if (h.contains(ERROR_MESSAGE)) {
            throw new HypervisorOperationException(h.length() < 100 ? h : h.substring(0, 100));
        }
    }
    @Override
    public void configureVirtualMachineHardware(int cores, int ram, VirtualMachineImage image) throws HypervisorOperationException {
    	if(cores!=0&&ram!=0){
            new Context(image.getMainFile().getPath()).changeVMXFileContext(cores,ram);
       }
    }
    @Override
    public boolean existsVirtualMachineSnapshot(VirtualMachineImage image, String snapshotname) throws HypervisorOperationException {
    	String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T",getType(),"listSnapshots",image.getMainFile().getPath());
    	return h!=null&&h.contains(snapshotname);
    }
    @Override
    public void restoreVirtualMachineSnapshot(VirtualMachineImage image, String snapshotname) throws HypervisorOperationException {
    	String h = LocalProcessExecutor.executeCommandOutput(getExecutablePath(),"-T",getType(),"revertToSnapshot",image.getMainFile().getPath(),snapshotname);
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
	}
	@Override
	public void unregisterVirtualMachine(VirtualMachineImage image) {
	}
	public abstract String getType();
}