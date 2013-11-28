package fileManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataServerSocket extends Thread{
	private DataServerSocket(){}
	private static DataServerSocket instance;
	private ExecutorService threadPool=Executors.newSingleThreadExecutor();
	public static void startServices(){
		if(instance==null){
			instance=new DataServerSocket();
			instance.start();
		}
	}
	@Override
	public void run(){
		try(ServerSocket ss = new ServerSocket(3020)){
			while(true)threadPool.submit(new FileTransferTask(ss.accept()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new DataServerSocket().start();
	}
}