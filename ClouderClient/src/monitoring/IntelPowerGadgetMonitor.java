package monitoring;

import java.io.File;

import virtualMachineManager.LocalProcessExecutor;

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
			long l=System.currentTimeMillis();
			try {
				System.out.println("Writing file: "+l);
				LocalProcessExecutor.executeCommandOutput("C:\\Program Files\\Intel\\Power Gadget 3.0\\PowerLog3.0.exe","-resolution","1000","-duration","120","-file",new File("energy",l+".txt").getAbsolutePath());
			} catch (Exception e) {
				System.out.println("Error on    : "+l);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
				return;
			}
		}
	}
	
}
