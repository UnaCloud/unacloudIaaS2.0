/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monitoring;
/**
 *
 * @author jcadavid
 */

import java.io.BufferedReader;
import java.sql.*;
import java.text.ParseException;
import physicalmachine.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
//import java.io.FileInputStream;
import java.io.FileWriter;
//import java.io.PrintStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
//import java.util.Properties;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nsclient4j.NSClient4JException;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Uptime;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.SigarCommandBase;

public class MonitorAgent extends SigarCommandBase {

    public static int ContadorRegistros;
    private static final String fmt = "MMM dd HH:mm:ss";
    private static final String fmt1 = "ddMMyyyyHHmmss";
    private static boolean continuarMonitoreo;
    private String UUID;
    public int parameter;
    public int contador=0;
    //int ContadorRegistros;
    Date parameter1;
    PrintWriter salida, salida2 = null;
    BufferedWriter bw, bw2 =null;
    FileWriter fw,fw2 =null;
    DatabaseConnection conexionBD=new DatabaseConnection();
    
    public MonitorAgent()
    {
        ContadorRegistros=0;
    }

    void CargarDatosIniciales() throws SQLException, NSClient4JException, ParseException, SigarException, IOException {
        conexionBD.Conectar();
        RegistrarDatosIniciales();
        iniciarRegistroEnDB();
    }

    public void IniciarMonitoreo(int frecuency, int registerFrecuency,boolean continuarMon) throws IOException, InterruptedException, SQLException, ParseException, SigarException, NSClient4JException, InstantiationException, IllegalAccessException
    {
        if(frecuency<10)frecuency=10;
        if(registerFrecuency<10)registerFrecuency=10;
        continuarMonitoreo=continuarMon;
        frecuency=frecuency*1000;
        registerFrecuency*=1000;
        conexionBD.Conectar();
        for (int i=0; i<frecuency; i+=registerFrecuency) {
            Registrar();
            MachineMonitor.monitorVirtualMachines();
            Thread.sleep(registerFrecuency);
            contador=contador+1;
        }
        conexionBD.RegistrarBatch();
        if (!continuarMonitoreo){
            try {
                TerminarMonitoreo(true);
            } catch (Throwable ex) {
                Logger.getLogger(MonitorAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void TerminarMonitoreo(boolean terminar) throws Throwable
    {
        if (terminar)
        {
            super.finalize();
        }        
    }

    //En este metodo se realiza un registro Inicial de los datos generales del Nodo
    private void RegistrarDatosIniciales () throws SQLException, ParseException, NSClient4JException, SigarException
    {
             PhysicalMachine monitor=new PhysicalMachine();
             java.util.Date date;
             date=new Date();
             java.sql.Timestamp timest = new java.sql.Timestamp(date.getTime());
             UUID=java.util.UUID.randomUUID().toString();
             //com.sun.security.auth.module.NTSystem NTSystem = new com.sun.security.auth.module.NTSystem();
             //Evalua una sola CPU
             org.hyperic.sigar.CpuInfo[] infos = this.sigar.getCpuInfoList();
             org.hyperic.sigar.CpuInfo CPU1 = infos[0];
             //System.out.println("Iniciando Carga de Batch Inicial");
             String domain="Corregir domain";
             conexionBD.CargarBatchInicial(UUID,timest,monitor.network.getHostname(),domain,monitor.operatingSystem.getOperatingSystemName(),
                     monitor.operatingSystem.getOperatingSystemVersion(),monitor.operatingSystem.getOperatingSystemArchitect(),monitor.cpu.getCPUModel(),
                     monitor.cpu.getCPUVendor(),monitor.cpu.getCPUCores(),CPU1.getTotalSockets(),monitor.cpu.getCPUMhz(),CPU1.getCoresPerSocket(),
                     monitor.memory.getRAMMemorySize(),monitor.memory.getSwapMemorySize(),monitor.hardDisk.getHardDiskSpace(),monitor.hardDisk.getHardDiskFileSystem(),
                     monitor.network.getNetworkMACAddress());
             //System.out.println("Terminando Carga de Batch Inicial");
             //salida.flush();
    }

    //En este metodo se realiza un registro full de todos los parametros
    //private void Registrar(PrintWriter salida) throws SQLException
    public void Registrar () throws SQLException, ParseException, NSClient4JException, SigarException, InstantiationException, IllegalAccessException, IOException
    {
            String UserName=null;
            String sFichero = "C:/dist/user.info";
            File fichero = new File(sFichero);
            if (fichero.exists())
            {
                //System.out.println("El fichero existe");
                FileReader fr = new FileReader("C:/dist/user.info");
                BufferedReader bf = new BufferedReader(fr);
                String User=null;
                while ((User = bf.readLine())!=null) {
                    UserName=User;
                }
                bf.close();
                fr.close();
            }
            else
            {
                //System.out.println("El fichero no existe");
                Process p=Runtime.getRuntime().exec("cmd.exe /c quser");
                InputStream is = p.getInputStream();
                BufferedReader br = new BufferedReader (new InputStreamReader (is));
                String linea;
                StringBuilder mensaje = new StringBuilder();
                while( (linea = br.readLine()) != null ){ 
                        if (linea.startsWith(">"))
                        {
                                int j = 0;    
                                String [] campos = linea.split("\\s+"); 
                                UserName=campos[0].substring(1, campos[0].length());
                        }
                        mensaje.append(linea+"\n");/*Concatenamos el mensaje*/
                    } 
                //String aux = br.readLine();              
            }
        
             LinpackJava.Linpack CPUMflops = new LinpackJava.Linpack();
             CPUMflops.run_benchmark();
             PhysicalMachine monitor=new PhysicalMachine();
             java.util.Date date;
             date=new Date();
             java.sql.Timestamp timest = new java.sql.Timestamp(date.getTime());
             ContadorRegistros ++;
             Cpu CPU3 = this.sigar.getCpu();
             CpuPerc CPU2 = this.sigar.getCpuPerc();
             Uptime UPTIME = this.sigar.getUptime();
             Mem MEM =  this.sigar.getMem();
             NetInterfaceStat NET = this.sigar.getNetInterfaceStat(monitor.network.getNetworkInterface());
             
             conexionBD.CargarBatch(UUID,timest,ContadorRegistros,UserName,UPTIME.getUptime(),CPUMflops.getMflops(),
                     CPUMflops.getTimeinSecs(),CPU2.getIdle()*100,(100-(CPU2.getIdle()*100)),CPU2.getUser()*100,CPU2.getSys()*100,CPU2.getNice()*100,
                     CPU2.getWait()*100,CPU2.getCombined()*100,CPU3.getUser(),CPU3.getSys(),CPU3.getNice(),CPU3.getWait(),CPU3.getIdle(),
                     monitor.memory.getRAMMemoryFree(),monitor.memory.getRAMMemoryUsed(),MEM.getFreePercent(),MEM.getUsedPercent(),
                     monitor.memory.getSwapMemoryFree(),monitor.memory.getSwapMemoryPageIn(),monitor.memory.getSwapMemoryPageOut(),
                     monitor.memory.getSwapMemoryUsed(),monitor.hardDisk.getHardDiskFreeSpace(),monitor.hardDisk.getHardDiskUsedSpace(),
                     monitor.network.getNetworkIPAddress(),monitor.network.getNetworkInterface(),monitor.network.getNetworkNetmask(),
                     monitor.network.getNetworkGateway(),NET.getRxBytes(),NET.getTxBytes(),NET.getSpeed(),NET.getRxErrors(),NET.getTxErrors(),
                     NET.getRxPackets(),NET.getTxPackets());

             //salida.flush();
    }

    public static Timestamp getTodayTimestamp ()
    {
        Calendar calendar=Calendar.getInstance();
        Timestamp timeStamp = new Timestamp(calendar.getTimeInMillis());
        return timeStamp;
    }

    private void iniciarRegistroEnDB() throws SQLException
    {
        conexionBD.RegistrarBatch();
    }

    @Override
    public void output(String[] strings) throws SigarException {
    }
}

