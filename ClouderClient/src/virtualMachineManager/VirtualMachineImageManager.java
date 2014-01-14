package virtualMachineManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.losandes.utils.VariableManager;

public class VirtualMachineImageManager {
	static String machineRepository="E:\\GRID\\";
	private static File imageListFile=new File("imageList");
	private static List<VirtualMachineImage> imageList=null;
	public static VirtualMachineImage getFreeImageCopy(long imageId){
		VirtualMachineImage vmi=lockFreeImageCopy(imageId);
		while(vmi==null){
			dowloadImageCopy(imageId);
			vmi=lockFreeImageCopy(imageId);
		}
		return vmi;
	}
	private synchronized static VirtualMachineImage lockFreeImageCopy(long imageId){
		loadImages();
		for(VirtualMachineImage vmi:imageList)if(vmi.getId()==imageId&&vmi.getStatus()==VirtualMachineImageStatus.FREE){
			vmi.setStatus(VirtualMachineImageStatus.LOCK);
			return vmi;
		}
		return null;
	}
	public synchronized static void freeLockedImageCopy(VirtualMachineImage vmi){
		vmi.setStatus(VirtualMachineImageStatus.FREE);
	}
	
	private synchronized static void addFreeImageCopy(VirtualMachineImage vmi){
		imageList.add(vmi);
		saveImages();
	}
	private static void dowloadImageCopy(long imageId){
		VirtualMachineImage vmi=new VirtualMachineImage();
		File root=new File(machineRepository+"\\"+imageId);
		cleanDir(root);
		root.mkdirs();
		final int puerto = VariableManager.global.getIntValue("DATA_SOCKET");
		final String ip=VariableManager.global.getStringValue("CLOUDER_SERVER_IP");
		try(Socket s=new Socket(ip,puerto);PrintWriter pw=new PrintWriter(s.getOutputStream())){
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
						String mainFile=br.readLine();
						if(mainFile==null){
							//Lanzar excepción
						}
						vmi.setMainFile(new File(root,mainFile));
						vmi.setPassword(br.readLine());
						vmi.setUsername(br.readLine());
						vmi.setStatus(VirtualMachineImageStatus.FREE);
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
			addFreeImageCopy(vmi);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	private static void cleanDir(File f){
		if(f.isDirectory())for(File r:f.listFiles())cleanDir(r);
		f.delete();
	}
	private static void saveImages(){
		try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(imageListFile))){
			oos.writeObject(imageList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void loadImages(){
		if(imageList==null){
			imageList=new ArrayList<>();
			try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(imageListFile))){
				imageList=(List<VirtualMachineImage>)ois.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
