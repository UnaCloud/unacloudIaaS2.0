/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package physicalmachine;

import com.losandes.utils.Log;
import com.losandes.utils.VirtualMachineCPUStates;
import communication.ServerMessageSender;
import java.io.File;
import java.util.Arrays;
/**
 *
 * @author Clouder
 */
public class VirtualMachineInfo {

    private File vmx;
    private String pid;
    private String vmeId;
    double cores;
    double mean;
    double[] last = new double[5];
    int cent=-1;
    VirtualMachineCPUStates state=VirtualMachineCPUStates.FREE;
    public VirtualMachineInfo(String vmeid,String route,int cores) {
        vmx = new File(route);
        this.cores=cores;
        vmeId=vmeid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public File getVmx() {
        return vmx;
    }

    public void setVmx(File vmx) {
        this.vmx = vmx;
    }

    public void updateCPU(double cpu){
        cent=(cent+1)%last.length;
        mean+=cpu;
        mean-=last[cent];
        last[cent]=cpu;
        if(!state.equals(VirtualMachineCPUStates.BUSY)&&mean/last.length>=0.8){
            state=VirtualMachineCPUStates.BUSY;
            ServerMessageSender.reportVirtualMachineCPUState(vmeId,VirtualMachineCPUStates.BUSY);
        }else if(state.equals(VirtualMachineCPUStates.BUSY)&&mean/last.length<0.4){
            state=VirtualMachineCPUStates.FREE;
            ServerMessageSender.reportVirtualMachineCPUState(vmeId,VirtualMachineCPUStates.FREE);
        }
        
    }

}
