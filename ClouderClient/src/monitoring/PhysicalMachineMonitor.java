package monitoring;


import java.util.ArrayList;

import com.losandes.utils.VariableManager;

public class PhysicalMachineMonitor extends Thread {
	
	public static boolean status;
		
	private static PhysicalMachineMonitor instance;
	public static synchronized PhysicalMachineMonitor getInstance(){
		if(instance==null)instance=new PhysicalMachineMonitor();
		return instance;
	}
	private static final ArrayList<AbstractMonitor> services = new ArrayList<AbstractMonitor>();
    //"C:\\Agentes\\UnaCloud\\logCPU.txt"
	//"C:\\Agentes\\UnaCloud\\report.txt"
	
	public PhysicalMachineMonitor() {}	
	
	public void initService(){	
		if(services.size()>0&&status){
			System.out.println("There are monitoring services running");
		}
		if(VariableManager.local.getBooleanValue("MONITORING_ENABLE")){
			services.clear();
			int frE = VariableManager.global.getIntValue("FRECUENCY_ENERGY");
			int frC = VariableManager.global.getIntValue("FRECUENCY_CPU");
			String path = VariableManager.local.getStringValue("PATH_POWERLOG");
			int time  = (int)(Math.random()*60*60);					
			if(frC>0){
				MonitorCPUAgent m = new MonitorCPUAgent(VariableManager.local.getStringValue("LOG_CPU_PATH"));
				m.setFrecuency(frC); 
				m.setWindowSizeTime(VariableManager.global.getIntValue("MONITOR_REGISTER_FREQUENCY_CPU"));
				m.setReduce(time);
				services.add(m);
			}					
			if(path!=null&&frE>0){
				MonitorEnergyAgent me = new MonitorEnergyAgent(VariableManager.local.getStringValue("LOG_ENERGY_PATH"));
				me.setPowerlogPath(path);
				me.setFrecuency(frE);			
				me.setWindowSizeTime(VariableManager.global.getIntValue("MONITOR_REGISTER_FREQUENCY_ENERGY"));
				me.setReduce(time);
				services.add(me);
			}
			for (AbstractMonitor abstractMonitor : services) abstractMonitor.run();
			status = true;
		}			
	}	
	
	public void stopService(){		
		for (AbstractMonitor abstractMonitor : services) abstractMonitor.stopExecution();
		status=false;
		VariableManager.local.setBooleanValue("MONITORING_ENABLE", false);
	}
	
	public void enabledService(){
		VariableManager.local.setBooleanValue("MONITORING_ENABLE", true);
		initService();
	}
	
	public void updateService(){
		int frE = VariableManager.global.getIntValue("FRECUENCY_ENERGY");
		int frC = VariableManager.global.getIntValue("FRECUENCY_CPU");
		String path = VariableManager.local.getStringValue("PATH_POWERLOG");
		String logCpu = VariableManager.local.getStringValue("LOG_CPU_PATH");
		String logEnergy = VariableManager.local.getStringValue("LOG_ENERGY_PATH");
		int wsCpu = VariableManager.global.getIntValue("MONITOR_REGISTER_FREQUENCY_CPU");
		int wsEnergy = VariableManager.global.getIntValue("MONITOR_REGISTER_FREQUENCY_ENERGY");
		int time  = (int)(Math.random()*60*60);		
		for (AbstractMonitor abstractMonitor : services) {
			if(abstractMonitor instanceof MonitorEnergyAgent){
				abstractMonitor.setFrecuency(frE);
				abstractMonitor.setWindowSizeTime(wsEnergy);
				abstractMonitor.setRecordPath(logEnergy);				
				((MonitorEnergyAgent) abstractMonitor).setPowerlogPath(path);
			}else if(abstractMonitor instanceof MonitorCPUAgent){
				abstractMonitor.setFrecuency(frC);
				abstractMonitor.setWindowSizeTime(wsCpu);
				abstractMonitor.setRecordPath(logCpu);
			}
			abstractMonitor.setReduce(time);
		}
	}
	//To testing
	
	//MonitorCPUAgent m = new MonitorCPUAgent("logCPU.txt");
	//private static int LIMIT_TIME = 60;
	//	int frE = 10;
	//	int frC = 10;
	//	String path = "C:\\Program Files\\Intel\\Power Gadget 3.0\\PowerLog3.0.exe";
}
