package monitoring;

import java.io.File;

public class IntelPowerGadgetMonitor extends Thread{
	public static void startMonitoring(){
		System.out.println("startMonitoring");
		new IntelPowerGadgetMonitor().start();
	}
	@Override
	public void run(){
		new File("energy").mkdirs();
		System.out.println("energy folder created");
		while(true){
			try {
				Process p=Runtime.getRuntime().exec(new String[]{"C:\\Program Files\\Intel\\Power Gadget 3.0\\PowerLog3.0.exe","-resolution","20000","-duration","1200","-file",new File("energy",System.currentTimeMillis()+".txt").getAbsolutePath()});
				p.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
}
