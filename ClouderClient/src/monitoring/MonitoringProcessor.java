package monitoring;


import com.losandes.utils.VariableManager;

public class MonitoringProcessor extends Thread {

	//MonitorCPUAgent m = new MonitorCPUAgent("C:\\Agentes\\UnaCloud\\reportes\\logCPU.txt");
	MonitorCPUAgent m = new MonitorCPUAgent("logCPU.txt");
	MonitorEnergyAgent me = new MonitorEnergyAgent("C:\\Agentes\\UnaCloud\\reportes\\report.txt");
	public MonitoringProcessor() {}	
	
	//Time 3 hours
	//private static int LIMIT_TIME = 3*60*60;
	private static int LIMIT_TIME = 60;
	
	public void initProcessor(){	
//		int frE = VariableManager.local.getIntValue("FRECUENCY_ENERGY");
//		int frC = VariableManager.local.getIntValue("FRECUENCY_CPU");
//		String path = VariableManager.local.getStringValue("PATH_POWERLOG");
		int frE = 10;
		int frC = 10;
		String path = "C:\\Program Files\\Intel\\Power Gadget 3.0\\PowerLog3.0.exe";
		int time  = (int)(Math.random()*60*60);		
		
		if(frC>0){
			m.setFrecuency(frC); 
			m.setWindowSizeTime(LIMIT_TIME);
			m.setReduce(time);
			m.start(); 	
		}		
		
		if(path!=null&&frE>0){
			me.setPowerlogPath(path);
			me.setFrecuency(frE);			
			me.setWindowSizeTime(LIMIT_TIME);
			me.setReduce(time);
			me.start();
		}
	}	
}
