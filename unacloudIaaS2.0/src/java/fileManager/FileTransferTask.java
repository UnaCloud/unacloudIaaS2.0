package fileManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import unacloud2.VirtualMachineImage;

public class FileTransferTask implements Runnable{
	VirtualMachineImage image;
	Socket s;
	public FileTransferTask(VirtualMachineImage image, Socket s) {
		this.image = image;
		this.s = s;
	}
	@Override
	public void run() {
		try{
			ZipOutputStream zos=new ZipOutputStream(s.getOutputStream());
			final byte[] buffer=new byte[1024*100];
			for(java.io.File f:new java.io.File(image.getMainFile()).getParentFile().listFiles())if(f.isFile()){
				zos.putNextEntry(new ZipEntry(f.getName()));
				try(FileInputStream fis=new FileInputStream(f)){
					for(int n;(n=fis.read(buffer))!=-1;)zos.write(buffer);
				}
				zos.closeEntry();
			}
			zos.flush();
			zos.close();
			System.out.println("Envie todo.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}