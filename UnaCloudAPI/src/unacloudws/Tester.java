package unacloudws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Random;

import unacloudws.UnaCloudOperations;
import unacloudws.requests.VirtualImageRequest;
import unacloudws.responses.VirtualMachineExecutionWS;
import unacloudws.responses.VirtualMachineStatusEnum;

public class Tester {
	public static void main(String[] args){
		try{
			int number=Integer.parseInt(args[0]);
			ClusterDescription c=startCluster(number);
			PrintWriter pw=new PrintWriter(new FileOutputStream(c.id+"/times.txt"),true);
			if(c!=null&&c.vms!=null&&!c.vms.isEmpty()){
				Thread[] ts=new Thread[number];
				for(int e=0;e<ts.length;e++)ts[e]=new HiloVm(e,c.id,c.vms.get(e).getVirtualMachineExecutionIP());
				pw.println("Arrandando en "+new Date());
				for(int e=0;e<ts.length;e++)ts[e].start();
				for(int e=0;e<ts.length;e++)ts[e].join();
				pw.println("Terminando en "+new Date());
			}
			if(c!=null)apagarCluster(c.id);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	public static ClusterDescription startCluster(int size)throws Exception{
		UnaCloudOperations uop=new UnaCloudOperations("admin","XT2PRB591GOJU5J9CTUP8EHK06IH37Y1");
		String deploymentId=uop.startHeterogeneousVirtualCluster(4,180,createRandomCluster(size));
		ClusterDescription desc=new ClusterDescription(-1,null);
		if(deploymentId!=null&&deploymentId.contains("\"id\"")){
			int ini=deploymentId.indexOf("\"id\"");
			int fin=deploymentId.indexOf(",",ini);
			ini=deploymentId.indexOf(":",ini);
			System.out.println("El id es "+deploymentId.substring(ini,fin));
			desc.id=Long.parseLong(deploymentId.substring(ini+1,fin));
			new File(""+desc.id).mkdirs();
			for(int encendidas=0;encendidas<size;encendidas=0){
				Thread.sleep(60000);
				List<VirtualMachineExecutionWS> vms=uop.getDeploymentInfo((int)desc.id);
				for(VirtualMachineExecutionWS vm:vms){
					if(vm.getVirtualMachineExecutionStatus()==VirtualMachineStatusEnum.DEPLOYED)encendidas++;
					if(vm.getVirtualMachineExecutionStatus()==VirtualMachineStatusEnum.FAILED){
						System.out.println("El cluster fallo, inicie de nuevo.");
						return desc;
					}
				}
				if(encendidas==vms.size()){
					desc.vms=vms;
					PrintWriter pw=new PrintWriter(desc.id+"/ips.txt");
					for(VirtualMachineExecutionWS vm:vms)pw.println(vm.getVirtualMachineExecutionIP());
					pw.close();
					break;
				}
				System.out.println(" Encendidas "+encendidas+"/"+vms.size());
			}
		}
		System.out.println("El cluster con id "+desc.id+" ha iniciado exitosamente");
		return desc;
	}
	public static void apagarCluster(long id)throws Exception{
		UnaCloudOperations uop=new UnaCloudOperations("admin","XT2PRB591GOJU5J9CTUP8EHK06IH37Y1");
		uop.stopDeployment((int)id);
		Thread.sleep(120000);
	}
	public static VirtualImageRequest[] createRandomCluster(int n){
		VirtualImageRequest[] ret=new VirtualImageRequest[n];
		for(int e=0;e<n;e++){
			int c=getCores();
			ret[e]=new VirtualImageRequest(1,c*1024,c,2);
		}
		return ret;
	}
	public static int getCores(){
		int c;
		do{
			c=r.nextInt(4)+1;
		}while(c==3);
		return c;
	}
	static class ClusterDescription{
		long id;
		List<VirtualMachineExecutionWS> vms;
		public ClusterDescription(long id, List<VirtualMachineExecutionWS> vms) {
			this.id = id;
			this.vms = vms;
		}
	}
	static class HiloVm extends Thread{
		String ip;
		int id;
		long deploymentId;
		public HiloVm(int id,long deploymentId,String ip) {
			this.ip = ip;
			this.id=id;
			this.deploymentId=deploymentId;
		}
		@Override
		public void run() {
			try {
				Process p=runtime.exec(new String[]{"sshpass","-p","DebianP445","ssh","-o","StrictHostKeyChecking=no",ip,"mdrun -v -s MD.tpr"});
				try(PrintWriter pw=new PrintWriter(new File(deploymentId+"",id+".out"))){
					pw.println(new Date());
					try(BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()))){
						for(String h;(h=br.readLine())!=null;)pw.println(h);
					}
					p.waitFor();
					pw.println(new Date());
				}
				System.out.println(" El proceso "+id+" termino con codigo "+p.waitFor());
				p.destroy();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	static Random r=new Random();
	static Runtime runtime=Runtime.getRuntime();
	
}
