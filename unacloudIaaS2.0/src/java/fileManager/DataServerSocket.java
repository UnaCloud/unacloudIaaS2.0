package fileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import unacloud2.VirtualMachineImage;
import unacloud2.VirtualMachineImageService;

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
		System.out.println("startServices DataServerSocket");
		try(ServerSocket ss = new ServerSocket(3020)){
			while(true){
				try{
					Socket s=ss.accept();
					BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
					long imageId=Long.parseLong(br.readLine());
					System.out.println("Atendiendo "+imageId);
					VirtualMachineImage image=new VirtualMachineImageService().getImage(imageId);
					image.setMainFile("C:\\VMs\\DebianPaaS64\\DebianPaaS64.vmx");
					threadPool.submit(new FileTransferTask(image, s));
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}