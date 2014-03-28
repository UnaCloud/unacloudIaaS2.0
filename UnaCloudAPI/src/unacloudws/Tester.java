package unacloudws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import unacloudws.UnaCloudOperations;
import unacloudws.requests.VirtualImageRequest;
import unacloudws.responses.VirtualMachineExecutionWS;
import unacloudws.responses.VirtualMachineStatusEnum;

public class Tester {
	public static void main(String[] args){
		final int times=Integer.parseInt(args[0]);
		final int number=Integer.parseInt(args[1]);
		for(int i=0;i<times;i++){
			try{
				ClusterDescription c=startCluster(number);
				PrintWriter pw=new PrintWriter(new FileOutputStream(c.id+"/times.txt"),true);
				if(c!=null&&c.vms!=null&&!c.vms.isEmpty()){
					Thread[] ts=new Thread[number];
					for(int e=0;e<ts.length;e++){
						ts[e]=new HiloVm(c.vms.get(e).getId(),c.id,c.vms.get(e).getVirtualMachineExecutionIP());
					}
					pw.println("Arrandando en "+new Date());
					for(int e=0;e<ts.length;e++)ts[e].start();
					for(int e=0;e<ts.length;e++)ts[e].join();
					pw.println("Terminando en "+new Date());
				}else{
					i--;
				}
				pw.close();
				if(c!=null)apagarCluster(c.id);
			}catch(Exception ex){
				ex.printStackTrace();
			}
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
			final long startTime=System.currentTimeMillis();
			for(int encendidas=0;encendidas<size;encendidas=0){
				Thread.sleep(60000);
				List<VirtualMachineExecutionWS> vms=uop.getDeploymentInfo((int)desc.id);
				if(vms==null){
					System.out.println("El cluster fallo, inicie de nuevo.");
					return desc;
				}
				for(VirtualMachineExecutionWS vm:vms)if(vm.getVirtualMachineExecutionStatus()==VirtualMachineStatusEnum.DEPLOYED)encendidas++;
				
				if(encendidas==vms.size()||(System.currentTimeMillis()-startTime)>=20l*60000l){
					desc.vms=new ArrayList<VirtualMachineExecutionWS>();
					for(VirtualMachineExecutionWS vm:vms)if(vm.getVirtualMachineExecutionStatus()==VirtualMachineStatusEnum.DEPLOYED)desc.vms.add(vm);
					PrintWriter pw=new PrintWriter(desc.id+"/ips.txt");
					for(VirtualMachineExecutionWS vm:vms)pw.println(vm.getVirtualMachineExecutionIP()+" "+vm.getVirtualMachineExecutionStatus());
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
		ciclo1:while(true){
			for(int e=0,s=0;e<n;e++){
				int c=getCores();
				ret[e]=new VirtualImageRequest(1,c*1024,c,2,"prueba");
				s+=c;
				if(s>436)continue ciclo1;
			}
			break;
		}
		return ret;
	}
	public static int getCores(){
		switch (r.nextInt(3)) {
			case 0:return 1;
			case 1:return 2;
			case 2:return 4;
		}
		return 0;
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
