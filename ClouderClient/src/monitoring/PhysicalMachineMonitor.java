package monitoring;

import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.losandes.utils.VariableManager;

/**
 * Class responsible for manage the physical machine monitor process
 * 
 * @author Clouder
 */
public class PhysicalMachineMonitor{

	private PhysicalMachineMonitor(){}

	/**
	 * Monitor agent bind
	 */
	private static MonitorAgent monitor;

	/**
	 * Restarts this physical machine monitor
	 */
	public static void restart() {
		try {
			boolean monitorear = Boolean.parseBoolean(VariableManager.getStringValue("MONITORING_ENABLE"));
			try {
				if (monitorear) {
					int monitorFrecuency = VariableManager.getIntValue("MONITOR_FREQUENCY");
					int monitorRegisterFrecuency = VariableManager.getIntValue("MONITOR_REGISTER_FREQUENCY");
					start(monitorFrecuency, monitorRegisterFrecuency);
				}
			} catch (Exception e) {
				monitorear = false;
			}
		} catch (Throwable t) {
		}
	}

	/**
	 * Starts this monitor with the given monitor and register frequency
	 * @param registerFrecuency time in seconds between each physical machine
	 *        measure
	 * @param dbFrecuency time in secconds between each data comit
	 */
	public static void start(final int registerFrecuency, final int dbFrecuency) {
		System.out.println("start monitoreo");
		if (monitor == null) {
			monitor = new MonitorAgent();
			try {
				monitor.CargarDatosIniciales();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			new Thread(){
				@Override
				public void run() {
					try {
						while (monitor != null)
							monitor.iniciarMonitoreo(dbFrecuency, registerFrecuency);
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
	public static void stop() {
		try {
			monitor = null;
		} catch (Throwable ex) {
			Logger.getLogger(PhysicalMachineMonitor.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			PrintWriter pw = new PrintWriter("monitoreo.txt");
			pw.println("NO");
			pw.close();
		} catch (Exception e) {
		}
	}
}
