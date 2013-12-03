package virtualMachineManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class VirtualMachineImageManager {
	static String machineRepository="D:\\GRID\\";
	static List<VirtualMachineImage> imageList=new ArrayList<>();
	public synchronized static VirtualMachineImage getFreeImageCopy(long imageId){
		for(VirtualMachineImage vmi:imageList)if(vmi.getId()==imageId){
			return vmi;
		}
		return getImageFiles(imageId);
	}
	private static VirtualMachineImage getImageFiles(long imageId){
		VirtualMachineImage vmi=new VirtualMachineImage();
		File root=new File(machineRepository+"\\"+imageId);
		root.mkdirs();
		try(Socket s=new Socket("127.0.0.1",3020);PrintWriter pw=new PrintWriter(s.getOutputStream())){
			pw.println(imageId);
			pw.flush();
			try(ZipInputStream zis=new ZipInputStream(s.getInputStream())){
				byte[] buffer=new byte[1024*100];
				for(ZipEntry entry;(entry=zis.getNextEntry())!=null;){
					System.out.println(entry.getName());
					if(entry.getName().equals("unacloudinfo")){
						BufferedReader br=new BufferedReader(new InputStreamReader(zis));
						vmi.setHypervisorId(br.readLine());
						vmi.setId(imageId);
						vmi.setMainFile(new File(root,br.readLine()));
						vmi.setPassword(br.readLine());
						vmi.setUsername(br.readLine());
						vmi.setVirtualMachineName(br.readLine());
						vmi.setConfiguratorClass(br.readLine());
						System.out.println("Configurator class is "+vmi.getConfiguratorClass());
					}else{
						try(FileOutputStream fos=new FileOutputStream(new File(root,entry.getName()))){
							for(int n;(n=zis.read(buffer))!=-1;){
								fos.write(buffer,0,n);
							}						
						}
					}
					zis.closeEntry();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		imageList.add(vmi);
		return vmi;
	}
}
