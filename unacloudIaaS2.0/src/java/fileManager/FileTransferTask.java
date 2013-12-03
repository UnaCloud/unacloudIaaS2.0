package fileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.losandes.utils.Constants;

import unacloud2.VirtualMachineImage;
import unacloud2.VirtualMachineImageService;

public class FileTransferTask implements Runnable{
	Socket s;
	public FileTransferTask(Socket s) {
		this.s = s;
	}
	@Override
	public void run() {
		try(Socket ss=s;BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));OutputStream os=s.getOutputStream()){
			ZipOutputStream zos=new ZipOutputStream(os);
			long imageId=Long.parseLong(br.readLine());
			System.out.println("Atendiendo "+imageId);
			VirtualMachineImage image=new VirtualMachineImageService().getImage(imageId);
			final byte[] buffer=new byte[1024*100];
			System.out.println("Enviando archivos");
			for(java.io.File f:new java.io.File(image.getMainFile()).getParentFile().listFiles())if(f.isFile()){
				zos.putNextEntry(new ZipEntry(f.getName()));
				System.out.println(f.getName());
				try(FileInputStream fis=new FileInputStream(f)){
					for(int n;(n=fis.read(buffer))!=-1;)zos.write(buffer,0,n);
				}
				zos.closeEntry();
			}
			zos.putNextEntry(new ZipEntry("unacloudinfo"));
			try(PrintWriter pw=new PrintWriter(zos)){
				pw.println(Constants.VM_WARE_WORKSTATION);
				pw.println(new File(image.getMainFile()).getName());
				pw.println(image.getPassword());
				pw.println(image.getUser());
				pw.println(image.getName());
				pw.println(image.getOperatingSystem().getConfigurer());
			}
			zos.closeEntry();
			zos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}