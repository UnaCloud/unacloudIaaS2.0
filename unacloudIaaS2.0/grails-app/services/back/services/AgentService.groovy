package back.services

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import communication.messages.ao.UpdateAgentMessage;
import javassist.bytecode.stackmap.BasicBlock.Catch;
import unacloud2.PhysicalMachine;
import unacloud2.ServerVariable;

class AgentService {
	VariableManagerService variableManagerService
	
    def updateMachine(PhysicalMachine pm){
		String ipAddress=pm.ip.ip;
		try{
			Socket s=new Socket(ipAddress,variableManagerService.getIntValue("CLOUDER_CLIENT_PORT"));
			UpdateAgentMessage updateMessage=new UpdateAgentMessage();
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(oos);
			oos.flush();
			oos.close();
			s.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	def copyAgentOnStream(OutputStream outputStream){
		ZipOutputStream zos=new ZipOutputStream(outputStream);
		copyFile(zos,"ClouderClient.jar",new File("web-app/agentSources/ClouderClient.jar"),true);
		zos.putNextEntry(new ZipEntry("vars"));
		PrintWriter pw=new PrintWriter(zos);
		for(ServerVariable sv:ServerVariable.all)pw.println(sv.serverVariableType.type+"."+sv.name+"="+sv.variable);
		pw.flush();
		zos.closeEntry();
		zos.close();
	}
	def copyUpdaterOnStream(OutputStream outputStream){
		ZipOutputStream zos=new ZipOutputStream(outputStream);
		copyFile(zos,"ClientUpdater.jar",new File("web-app/agentSources/ClientUpdater.jar"),true);
		zos.putNextEntry(new ZipEntry("vars"));
		PrintWriter pw=new PrintWriter(zos);
		for(ServerVariable sv:ServerVariable.all)pw.println(sv.serverVariableType.type+"."+sv.name+"="+sv.variable);
		pw.flush();
		zos.closeEntry();
		zos.close();
	}
	private static void copyFile(ZipOutputStream zos,String filePath,File f,boolean root)throws IOException{
		if(f.isDirectory())for(File r:f.listFiles())copyFile(zos,(root?"":(filePath+"/"))+r.getName(),r,false);
		else if(f.isFile()){
			zos.putNextEntry(new ZipEntry(filePath));
			Files.copy(f.toPath(),zos);
			zos.closeEntry();
		}
	}
}
