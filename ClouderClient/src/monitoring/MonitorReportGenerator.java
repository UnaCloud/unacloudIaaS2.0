package monitoring;

import communication.messages.monitoring.MonitorInitialReport;
import communication.messages.monitoring.MonitorReport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Uptime;
import org.hyperic.sigar.cmd.SigarCommandBase;

import physicalmachine.Network;
import physicalmachine.PhysicalMachine;

public class MonitorReportGenerator extends SigarCommandBase {

    private static MonitorReportGenerator instance = new MonitorReportGenerator();
    private static String UUID = java.util.UUID.randomUUID().toString();
    ;
	private static int contadorRegistros;

    public static MonitorInitialReport generateInitialReport() {
        PhysicalMachine monitor = new PhysicalMachine();
        java.util.Date date;
        date = new Date();
        java.sql.Timestamp timest = new java.sql.Timestamp(date.getTime());
        // com.sun.security.auth.module.NTSystem NTSystem = new
        // com.sun.security.auth.module.NTSystem();
        // Evalua una sola CPU
        CpuInfo[] infos;
        try {
            infos = instance.sigar.getCpuInfoList();
            RepetitionCounter cpuModel = new RepetitionCounter();
            RepetitionCounter cpuVendor = new RepetitionCounter();
            RepetitionCounter cpuMhz = new RepetitionCounter();

            for (CpuInfo cpu : infos) {
                cpuModel.add(cpu.getModel());
                cpuVendor.add(cpu.getVendor());
                cpuMhz.add("" + cpu.getMhz());

            }
            org.hyperic.sigar.CpuInfo CPU1 = infos[0];
            return new MonitorInitialReport(UUID, timest,
                    Network.getHostname(),
                    monitor.operatingSystem.getOperatingSystemName(),
                    monitor.operatingSystem.getOperatingSystemVersion(),
                    monitor.operatingSystem.getOperatingSystemArchitect(),
                    cpuModel.toString(), cpuVendor.toString(),
                    monitor.cpu.getCPUCores(), CPU1.getTotalSockets(),
                    cpuMhz.toString(), CPU1.getCoresPerSocket(),
                    monitor.memory.getRAMMemorySize(),
                    monitor.memory.getSwapMemorySize(),
                    monitor.hardDisk.getHardDiskSpace(),
                    monitor.hardDisk.getHardDiskFileSystem(),
                    monitor.network.getNetworkMACAddress());
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class RepetitionCounter extends HashMap<String, Integer> {

		private static final long serialVersionUID = -5259022218756213835L;

		public void add(String input) {
            Integer n = get(input);
            if (n == null) {
                n = 0;
            }
            n++;
            put(input, n);
        }

        @Override
        public String toString() {
            String ret = null;
            for (java.util.Map.Entry<String, Integer> ent : entrySet()) {
                ret = (ret != null ? ";" : "") + ent.getKey() + " x" + ent.getValue();
            }
            return ret == null ? "" : ret;
        }
    }

    public static MonitorReport generateStateReport() {
        LinpackJava.Linpack CPUMflops = new LinpackJava.Linpack();
        CPUMflops.run_benchmark();
        PhysicalMachine monitor = new PhysicalMachine();
        java.sql.Timestamp timest = new java.sql.Timestamp(new Date().getTime());
        contadorRegistros++;

        String processes = "";
        try {
            long[] pids = instance.sigar.getProcList();
            for (long id : pids) {
                try {
                    String[] processName = instance.sigar.getProcExe(id).getName().split("\\\\");
                    processes += processName[processName.length - 1] + ",";
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
        }
        try {
            Cpu cpu = instance.sigar.getCpu();
            CpuPerc CPU2 = instance.sigar.getCpuPerc();
            Uptime UPTIME = instance.sigar.getUptime();
            Mem MEM = instance.sigar.getMem();
            NetInterfaceStat NET = instance.sigar
                    .getNetInterfaceStat(monitor.network.getNetworkInterface());
            return new MonitorReport(UUID, timest, contadorRegistros, getUserName(),
                    UPTIME.getUptime(), CPUMflops.getMflops(),
                    CPUMflops.getTimeinSecs(), CPU2.getIdle() * 100,
                    (100 - (CPU2.getIdle() * 100)), CPU2.getUser() * 100,
                    CPU2.getSys() * 100, CPU2.getNice() * 100,
                    CPU2.getWait() * 100, CPU2.getCombined() * 100, cpu.getUser(),
                    cpu.getSys(), cpu.getNice(), cpu.getWait(), cpu.getIdle(),
                    monitor.memory.getRAMMemoryFree(),
                    monitor.memory.getRAMMemoryUsed(), MEM.getFreePercent(),
                    MEM.getUsedPercent(), monitor.memory.getSwapMemoryFree(),
                    monitor.memory.getSwapMemoryPageIn(),
                    monitor.memory.getSwapMemoryPageOut(),
                    monitor.memory.getSwapMemoryUsed(),
                    monitor.hardDisk.getHardDiskFreeSpace(),
                    monitor.hardDisk.getHardDiskUsedSpace(),
                    monitor.network.getNetworkIPAddress(),
                    monitor.network.getNetworkInterface(),
                    monitor.network.getNetworkNetmask(),
                    monitor.network.getNetworkGateway(), NET.getRxBytes(),
                    NET.getTxBytes(), NET.getSpeed(), NET.getRxErrors(),
                    NET.getTxErrors(), NET.getRxPackets(), NET.getTxPackets(), processes);
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUserName() {
        String userName = null;
        try {
            Process p = Runtime.getRuntime().exec("cmd.exe /c quser");
            InputStream is = p.getInputStream();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                br.readLine();
                for (String linea; (linea = br.readLine()) != null;) {
                    String user = linea.trim().split(" |\t")[0];
                    userName = (userName == null ? "" : ";") + user;
                }
            }
        } catch (IOException ex) {
        }
        return userName;
    }

    @Override
    public void output(String[] arg0) throws SigarException {
        // TODO Auto-generated method stub
    }
}
