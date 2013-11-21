/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package physicalmachine;

import com.losandes.utils.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.SigarException;

/**
 *
 * @author Clouder
 */
public class MachineMonitor extends SigarWrapper{

    private static List<VirtualMachineInfo> machines = new ArrayList<VirtualMachineInfo>();

    public MachineMonitor() {

    }

    public static void removeMachineExecution(String vmxRoute){
        File f=new File(vmxRoute);
        for(int e=0;e<machines.size();e++)if(machines.get(e).getVmx().equals(f)){
            machines.remove(e);
            e--;
        }
    }

    public static void addMachineExecution(String id,String vmxRoute,int cores){
        VirtualMachineInfo vmi = new VirtualMachineInfo(id,vmxRoute,cores);
        try {
            Process p = Runtime.getRuntime().exec("tasklist");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            for(String h;(h=br.readLine())!=null;){
                if(h.contains("vmware-vmx")){
                    String pid = h.split(" +|\t+")[1];
                    try {
                        String cwd = sigar.getProcExe(pid).getCwd();
                        if(new File(cwd).equals(vmi.getVmx().getParentFile())){
                            vmi.setPid(pid);
                            machines.add(vmi);
                            break;
                        }
                    } catch (SigarException ex) {
                    }
                }
            }
            br.close();
        } catch (IOException ex) {
            Log.print("EM "+ex.getMessage());
        }
    }

    public static void monitorVirtualMachines(){
        for(int e=0,i=machines.size();e<i;e++){
            try{
                machines.get(e).updateCPU(sigar.getProcCpu(machines.get(e).getPid()).getPercent());
            }catch(Exception ex){
                Log.print("EM "+ex.getMessage());
            }
            
        }
    }

}
