package virtualMachineManager;

import hypervisorManager.Hypervisor;
import hypervisorManager.HypervisorFactory;
import hypervisorManager.VirtualBox;

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
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.losandes.utils.Constants;
import com.losandes.utils.VariableManager;

public class ImageCacheManager {
	static String machineRepository="E:\\GRID\\";
	private static File imageListFile=new File("imageList");
	private static Map<Long,Image> imageList=null;
	public static ImageCopy getFreeImageCopy(long imageId){
		Image vmi=getImage(imageId);
		synchronized (vmi) {
			if(vmi.imageCopies.isEmpty()){
				ImageCopy copy=new ImageCopy();
				dowloadImageCopy(vmi,copy);
			}else{
				for(ImageCopy copy:vmi.imageCopies){
					if(copy.getStatus()==VirtualMachineImageStatus.FREE){
						copy.setStatus(VirtualMachineImageStatus.LOCK);
						return copy;
					}
				}
				/*for(Image vmi:imageList)if(vmi.getId()==imageId){
				for(ImageCopy)
				if(vmi.getStatus()==VirtualMachineImageStatus.FREE){
				vmi.setStatus(VirtualMachineImageStatus.LOCK);
				return vmi;
			}
			for(Image vmi:imageList)if(vmi.getId()==imageId&&vmi.getStatus()==VirtualMachineImageStatus.LOCK){
				Image newImage=new Image();
				newImage.setHypervisorId(br.readLine());
				newImage.setId(imageId);
				newImage.setMainFile(new File(root,mainFile));
				newImage.setPassword(br.readLine());
				newImage.setUsername(br.readLine());
				newImage.setStatus(VirtualMachineImageStatus.FREE);
				newImage.setVirtualMachineName(br.readLine());
				newImage.setConfiguratorClass(br.readLine());
			}
			return null;*/
				//clonar?
			}
		}
		return null;
	}
	private synchronized static Image getImage(long imageId){
		loadImages();
		Image vmi=imageList.get(imageId);
		if(vmi==null){
			vmi=new Image();
			vmi.setId(imageId);
			imageList.put(imageId,vmi);
			saveImages();
		}
		return vmi;
	}
	public synchronized static void freeLockedImageCopy(ImageCopy vmiCopy){
		vmiCopy.setStatus(VirtualMachineImageStatus.FREE);
	}
	private static void dowloadImageCopy(Image image,ImageCopy copy){
		File root=new File(machineRepository+"\\"+image.getId()+"\\base");
		cleanDir(root);
		root.mkdirs();
		final int puerto = VariableManager.global.getIntValue("DATA_SOCKET");
		final String ip=VariableManager.global.getStringValue("CLOUDER_SERVER_IP");
		try(Socket s=new Socket(ip,puerto);PrintWriter pw=new PrintWriter(s.getOutputStream())){
			pw.println(image.getId());
			pw.flush();
			try(ZipInputStream zis=new ZipInputStream(s.getInputStream())){
				byte[] buffer=new byte[1024*100];
				for(ZipEntry entry;(entry=zis.getNextEntry())!=null;){
					if(entry.getName().equals("unacloudinfo")){
						BufferedReader br=new BufferedReader(new InputStreamReader(zis));
						image.setHypervisorId(br.readLine());
						String mainFile=br.readLine();
						if(mainFile==null){
							//Lanzar excepci�n
						}
						copy.setMainFile(new File(root,mainFile));
						image.setPassword(br.readLine());
						image.setUsername(br.readLine());
						copy.setStatus(VirtualMachineImageStatus.FREE);
						copy.setVirtualMachineName(br.readLine());
						image.setConfiguratorClass(br.readLine());
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
			Hypervisor hypervisor=HypervisorFactory.getHypervisor(image.getHypervisorId());
			hypervisor.registerVirtualMachine(copy);
			hypervisor.changeVirtualMachineMac(copy);
			hypervisor.takeVirtualMachineSnapshot(copy,"unacloudbase");
			hypervisor.unregisterVirtualMachine(copy);
			copy.setImage(image);
			image.imageCopies.add(copy);
			saveImages();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	private static void cleanDir(File f){
		if(f.isDirectory())for(File r:f.listFiles())cleanDir(r);
		f.delete();
	}
	public static synchronized String clearCache(){
		loadImages();
		imageList.clear();
		try{
			((VirtualBox)HypervisorFactory.getHypervisor(Constants.VIRTUAL_BOX)).unregisterAllVms();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		saveImages();
		return "Successful";
	}
	private static synchronized void saveImages(){
		try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(imageListFile))){
			oos.writeObject(imageList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	private static void loadImages(){
		if(imageList==null){
			imageList=new TreeMap<>();
			try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(imageListFile))){
				imageList=(Map<Long,Image>)ois.readObject();
				for(Image im:imageList.values())for(ImageCopy copy:im.imageCopies){
					copy.setStatus(VirtualMachineImageStatus.FREE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}