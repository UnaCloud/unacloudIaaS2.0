package monitoring;

import com.losandes.utils.Log;
import com.losandes.utils.VariableManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for manage the physical machine monitor process
 * @author Clouder
 */
public class PhysicalMachineMonitor {

    private PhysicalMachineMonitor() {
    }

    /**
     * Monitor agent bind
     */
    private static MonitorAgent monitor;

    /**
     * Boolean constant that determines if the monitor is active or not
     */
    private static volatile boolean cont;

    /**
     * Restarts this physical machine monitor
     */
    public static void restart(){
        try{
            boolean monitorear = Boolean.parseBoolean(VariableManager.getStringValue("MONITORING_ENABLE"));
            int fm=1,fbd=1;
            try{
                if(monitorear){
                    fm=VariableManager.getIntValue("MONITOR_FREQUENCY");
                    fbd=VariableManager.getIntValue("MONITOR_REGISTER_FREQUENCY");
                    start(fm, fbd);
                }
            }catch(Exception e){monitorear=false;}
        }catch(Throwable t){
        }
    }

    /**
     * Starts this monitor with the given monitor and register frequency
     * @param registerFrecuency time in seconds between each physical machine measure
     * @param dbFrecuency time in secconds between each data comit
     */
    public static void start(final int registerFrecuency,final int dbFrecuency){
        System.out.println("start monitoreo");
        cont=true;
        if(monitor==null){
            monitor = new MonitorAgent();
            try{monitor.CargarDatosIniciales();}catch(Exception  ex){ex.printStackTrace();}
            new Thread(){
                @Override
                public void run() {
                    try {
                        while(cont)monitor.IniciarMonitoreo(dbFrecuency, registerFrecuency, cont);
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
     * Stops the monitor process
     */
    public static void stop(){
        cont=false;
        try{
            PrintWriter pw = new PrintWriter("monitoreo.txt");
            pw.println("NO");
            pw.close();
        }catch(Exception e){}
        try {
            monitor.TerminarMonitoreo(true);
            monitor=null;
        } catch (Throwable ex) {
            Logger.getLogger(PhysicalMachineMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

