package fileManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataServerSocket extends Thread{
	private static DataServerSocket instance;
	private ExecutorService threadPool=Executors.newFixedThreadPool(3);
	private int listenPort;
	public DataServerSocket(int listenPort) {
		this.listenPort = listenPort;
	}
	public static void startServices(int port){
		if(instance==null){
			instance=new DataServerSocket(port);
			instance.start();
		}
	}
	@Override
	public void run(){
		System.out.println("starting ss on port "+listenPort);
		try(ServerSocket ss = new ServerSocket(listenPort)){
			while(true)threadPool.submit(new FileTransferTask(ss.accept()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}