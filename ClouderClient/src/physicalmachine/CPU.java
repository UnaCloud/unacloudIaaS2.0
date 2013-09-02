package physicalmachine;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
public class CPU extends SigarWrapper {

    private int cPUCores;

    private CpuPerc cpuPer;
    private Cpu cpu;
    private org.hyperic.sigar.CpuInfo[] infos;

    public CPU() {
    }

    private CpuPerc getCpuPer() {
        if(cpuPer==null)try {
            cpuPer = sigar.getCpuPerc();
        } catch (SigarException ex) {
        }
        return cpuPer;
    }

    private CpuInfo[] getInfos() {
        if(infos==null){
            try {
                infos = sigar.getCpuInfoList();
            } catch (SigarException ex) {
            }
        }
        return infos;
    }

    /**
     * @return the CPUidle
     */
    public String getCPUidle() {
        getCpuPer();
        double d = cpuPer.getIdle();
        return format.format(d);
    }

    /**
     * @return the CPUVendor
     */
    public String getCPUVendor() {
        getInfos();
        String h=infos.length>0?infos[0].getVendor():"";
        for(int e=1;e<infos.length;e++)h+=";"+infos[e].getVendor();
        return h;
    }

    /**
     * @return the CPUModel
     */
    public String getCPUModel() {
        getInfos();
        String h=infos.length>0?infos[0].getModel():"";
        for(int e=1;e<infos.length;e++)h+=";"+infos[e].getModel();
        return h;
    }

    /**
     * @return the CPUMhz
     */
    public String getCPUMhz() {
        getInfos();
        String h=infos.length>0?""+infos[0].getMhz():"";
        for(int e=1;e<infos.length;e++)h+=";"+infos[e].getMhz();
        return h;
    }

    /**
     * @return the CPUCores
     */
    public int getCPUCores() {
        return cPUCores;
    }

    /**
     * @return the cPuUsed
     */
    public String getCPUUsed() {
        getCpuPer();
        double d = cpuPer.getIdle();
        return format.format(100-d);
    }

    public Long getCPUUserTime() {
        getCPUCores();
        return cpu.getUser();
    }

    //retorna CPUUserTime
    public Long getCPUTotalTime() {
        getCPUCores();
        return cpu.getTotal();
    }

}//end of CPU

