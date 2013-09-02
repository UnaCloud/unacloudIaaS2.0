package virtualmachine;
import static com.losandes.utils.Constants.*;
import java.io.File;
import execution.LocalProcessExecutor;
/**
 * Implementation of hypervisor abstract class to give support for VMwarePlayer hypervisor.
 * @author Clouder
 */
class VMwarePlayer extends Hypervisor{

    @Override
    protected void setVirtualMachinePath(String virtualMachinePath) {
        virtualMachinePath=virtualMachinePath.replace("\"","");
        if (!virtualMachinePath.contains(VMW_VMX_EXTENSION))virtualMachinePath += VMW_VMX_EXTENSION;
        super.setVirtualMachinePath("\""+virtualMachinePath+"\"");
    }

    @Override
    protected void setExecutablePath(String executablePath) {
        executablePath=executablePath.replace("\"","");
        if(!executablePath.endsWith("vmrun.exe")){
            if(!executablePath.endsWith("\\")&&!executablePath.endsWith("/"))executablePath+="\\vmrun.exe";
            else executablePath += "vmrun.exe";
        }
        super.setExecutablePath("\""+executablePath+"\"");
    }

    @Override
    public void turnOnVirtualMachine()throws HypervisorOperationException{
        String h=LocalProcessExecutor.executeCommandOutput(getExecutablePath() + " -T player start "+getVirtualMachinePath()+" nogui");
        if(h.contains(ERROR_MESSAGE))throw new HypervisorOperationException(h.length()<100?h:h.substring(0,100));
    }

    @Override
    public void turnOffVirtualMachine() throws HypervisorOperationException{
        String h=LocalProcessExecutor.executeCommandOutput(getExecutablePath() + " -T player stop "+getVirtualMachinePath());
        if(h.contains(ERROR_MESSAGE))throw new HypervisorOperationException(h.length()<100?h:h.substring(0,100));
    }

    @Override
    public void restartVirtualMachine() throws HypervisorOperationException{
        String h=LocalProcessExecutor.executeCommandOutput(getExecutablePath() + " -T player reset "+getVirtualMachinePath());
        if(h.contains(ERROR_MESSAGE))throw new HypervisorOperationException(h.length()<100?h:h.substring(0,100));
    }

    @Override
    protected Hypervisor getInstance() {
        return new VMwarePlayer();
    }

    @Override
    public void preconfigureVirtualMachine(int coreNumber, int ramSize,String persistant) throws HypervisorOperationException {
        Context vmx = new Context(getVirtualMachinePath());
        vmx.changeVMXFileContext(String.valueOf(coreNumber).toString(), String.valueOf(ramSize).toString(),persistant!=null&&persistant.equals("true"));
    }

    @Override
    public void executeCommandOnMachine(String user, String pass, String command) throws HypervisorOperationException {
        pass=pass.replace(" ","").replace("\"","");
        user=user.replace(" ","").replace("\"","");
        String h=LocalProcessExecutor.executeCommandOutput(getExecutablePath() + " -T player -gu " + user + " -gp " + pass + " runProgramInGuest " + getVirtualMachinePath() + " " + command);
        if(h.contains(ERROR_MESSAGE))throw new HypervisorOperationException(h.length()<100?h:h.substring(0,100));
    }

    @Override
    public void copyFileOnVirtualMachine(String user, String pass, String destinationRoute, File sourceFile) throws HypervisorOperationException {
        pass=pass.replace(" ","").replace("\"","");
        user=user.replace(" ","").replace("\"","");
        String h=LocalProcessExecutor.executeCommandOutput(getExecutablePath() + " -T player -gu " + user + " -gp " + pass + " copyFileFromHostToGuest " + getVirtualMachinePath() + " \"" + sourceFile.getAbsolutePath()+"\"  " + destinationRoute);
        if(h.contains(ERROR_MESSAGE))throw new HypervisorOperationException(h.length()<100?h:h.substring(0,100));
    }

    @Override
    public void takeSnapshotOnMachine(String snapshotname) throws HypervisorOperationException {
        String h=LocalProcessExecutor.executeCommandOutput(getExecutablePath() + " -T player snapshot " + getVirtualMachinePath() + " " + snapshotname);
        if(h.contains(ERROR_MESSAGE))throw new HypervisorOperationException(h.length()<100?h:h.substring(0,100));
    }
}