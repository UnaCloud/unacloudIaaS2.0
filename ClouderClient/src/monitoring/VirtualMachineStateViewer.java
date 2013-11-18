package monitoring;

import communication.ServerMessageSender;
import execution.PersistentExecutionManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.losandes.utils.Log;
import static com.losandes.utils.Constants.*;
import virtualmachine.Hypervisor;

/**
 * This class is responsible for checking if a VMware virtual machine has been correctly deployed. That is, if the virtual machines has started and if it has well configured its IP address
 * @author Clouder
 */
public class VirtualMachineStateViewer {

    /**
     * constructs a VirtualMachineStateViewer for the given virtual machine. On this method the virtual machine is checked and the state is reported to UnaCloud server
     * @param virtualMachineCode The virtual machine id to make the virtual machine status report to the server
     * @param hypervisorPath The path of the hypervisor to be used to check the virtual machine status
     * @param vmIP The ip address to check if the virtual machine is accessible
     * @param vmPath The path of the virtual machine that must be checked
     * @param hypervisorName The type of hypervisor used to deploy the virtual machine
     */
    public VirtualMachineStateViewer(String virtualMachineCode,Hypervisor v,String vmIP){
        ServerMessageSender.reportVirtualMachineState(virtualMachineCode,DEPLOYING_STATE,"Starting virtual machine");
        /*boolean encendio=false,red=false;
        if(v.getHypervisorId()==VIRTUAL_BOX){
            encendio=true;
        }else{
            for(int e=0;e<2&&!encendio;e++){
                if(vmrunListVerification(v.getExecutablePath(),v.getExecutablePath())){
                    encendio=true;
                }
                try{Thread.sleep(10000);}catch(Exception ex){}
            }
        }*/
        boolean encendio=true,red=false;
        if(encendio)for(int e=0;e<8&&!red;e++){
            if(pingVerification(vmIP))red=true;
            if(!red)try{Thread.sleep(30000);}catch(Exception ex){}
        }
        if(encendio&&red)ServerMessageSender.reportVirtualMachineState(virtualMachineCode,ON_STATE,"Machine started");
        else{
            PersistentExecutionManager.removeExecution(v.getHypervisorId(),v.getVirtualMachinePath(),v.getExecutablePath(),virtualMachineCode);
            if(encendio&&!red)ServerMessageSender.reportVirtualMachineState(virtualMachineCode,ERROR_STATE,"Machine not configured");
            else if(!encendio&&red)ServerMessageSender.reportVirtualMachineState(virtualMachineCode,ERROR_STATE,"Machine didn't start");
            else if(!encendio&&!red)ServerMessageSender.reportVirtualMachineState(virtualMachineCode,ERROR_STATE,"Machine didn't start");
        }
    }

    /**
     * This method uses the vmrun command to ask for a running virtual machine list, then each running virtual machine is compared with the given one and if there is at least one equal it returns true.
     * @param hypervisorPath The path of the hypervisor executable to ask for its virtual machine list
     * @param vmPath The path of the virtual machine to be checked
     * @return Returns true if the virtual machine is started by a vmware product, false otherwise
     */
    private boolean vmrunListVerification(String hypervisorPath,String vmPath){
        boolean ret = false;
        if(!hypervisorPath.endsWith("/")&&!hypervisorPath.endsWith("\\"))hypervisorPath=hypervisorPath+"/";
        try {
            Process p = new ProcessBuilder(hypervisorPath+"vmrun.exe","list").start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int e = Integer.parseInt(br.readLine().split(":")[1].trim()),i=0;
            File vmx = new File(vmPath);
            for(String h;i<e&&(h=br.readLine())!=null;i++){
                if(new File(h).equals(vmx)){
                    Log.print("true");
                    ret=true;
                    br.close();
                    p.destroy();
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(VirtualMachineStateViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    /**
     * Pings the given address
     * @param vmIP The ip to be pinged
     * @return True if the given ip responds ping, false otherwise
     */
    private boolean pingVerification(String vmIP){
        try {
            Process p = Runtime.getRuntime().exec("ping " + vmIP + " -n 2");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            for(String h;(h=br.readLine())!=null;){
                if(h.contains("TTL")){
                    p.destroy();
                    br.close();
                    return true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(VirtualMachineStateViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


}
