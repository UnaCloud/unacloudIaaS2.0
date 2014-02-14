import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import unacloudws.UnaCloudOperations;
import unacloudws.requests.VirtualImageRequest;
import unacloudws.responses.VirtualMachineExecutionWS;
import unacloudws.responses.VirtualMachineStatusEnum;

public class Main {

	public static void main(String[] args)throws Exception{
		if(args.length>1){
			String operacion=args[0];
			int number=Integer.parseInt(args[1]);
			if(args[0].equals("start")){
				UnaCloudOperations uop=new UnaCloudOperations("admin","XT2PRB591GOJU5J9CTUP8EHK06IH37Y1");
				String deploymentId=uop.startHeterogeneousVirtualCluster(4,180,createRandomCluster(number));
				System.out.println("El id es "+deploymentId);
				long id=-1;
				if(deploymentId!=null&&deploymentId.contains("\"id\"")){
					int ini=deploymentId.indexOf("\"id\"");
					int fin=deploymentId.indexOf(",",ini);
					ini=deploymentId.indexOf(":",ini);
					System.out.println(deploymentId.substring(ini,fin));
					id=Long.parseLong(deploymentId.substring(ini+1,fin));
					while(true){
						Thread.sleep(60000);
						int encendidas=0;
						List<VirtualMachineExecutionWS> vms=uop.getDeploymentInfo((int)id);
						for(VirtualMachineExecutionWS vm:vms){
							if(vm.getVirtualMachineExecutionStatus()==VirtualMachineStatusEnum.DEPLOYED)encendidas++;
							if(vm.getVirtualMachineExecutionStatus()==VirtualMachineStatusEnum.FAILED){
								System.out.println("El cluster falló, inicie de nuevo.");
								return;
							}
						}
						if(encendidas==vms.size()){
							PrintWriter pw=new PrintWriter(id+".txt");
							for(VirtualMachineExecutionWS vm:vms)pw.println(vm.getVirtualMachineExecutionIP());
							pw.close();
							break;
						}
						System.out.println(" Encendidas "+encendidas+"/"+vms.size());
						
					}
				}
				System.out.println("El cluster con id "+id+" ha iniciado exitosamente");
			}else if(args[0].equals("stop")){
				UnaCloudOperations uop=new UnaCloudOperations("admin","XT2PRB591GOJU5J9CTUP8EHK06IH37Y1");
				uop.stopDeployment(number);
			}
		}
	}
	static Random r=new Random();
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
}
