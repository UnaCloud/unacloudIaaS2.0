package back.services

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import communication.UnaCloudAbstractMessage;
import communication.messages.ao.*;
import javassist.bytecode.stackmap.BasicBlock.Catch;
import unacloud2.PhysicalMachine;
import unacloud2.ServerVariable;

class AgentService {
	VariableManagerService variableManagerService
    def updateMachine(PhysicalMachine pm){
		sendMessage(pm,new UpdateAgentMessage());
	}
	def stopMachine(PhysicalMachine pm){
		sendMessage(pm,new StopAgentMessage());
	}
	def clearCache(PhysicalMachine pm){
		sendMessage(pm,new ClearVMCacheMessage());
	}
	def sendMessage(PhysicalMachine pm,UnaCloudAbstractMessage message){
		String ipAddress=pm.ip.ip;
		try{
			Socket s=new Socket(ipAddress,variableManagerService.getIntValue("CLOUDER_CLIENT_PORT"));
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			oos.writeObject(message);
			oos.flush();
			ois.readObject();
			s.close();
		}catch(Exception e){
			println "Error conectando a "+ipAddress;
		}
		try{
			Thread.sleep(500);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	def copyAgentOnStream(OutputStream outputStream,File appDir){
		ZipOutputStream zos=new ZipOutputStream(outputStream);
		copyFile(zos,"ClientUpdater.jar",new File(appDir,"agentSources/ClientUpdater.jar"),true);
		copyFile(zos,"ClouderClient.jar",new File(appDir,"agentSources/ClouderClient.jar"),true);
		zos.putNextEntry(new ZipEntry("vars"));
		PrintWriter pw=new PrintWriter(zos);
		for(ServerVariable sv:ServerVariable.all)pw.println(sv.serverVariableType.type+"."+sv.name+"="+sv.variable);
		pw.flush();
		zos.closeEntry();
		zos.close();
	}
	def copyUpdaterOnStream(OutputStream outputStream,File appDir){
		ZipOutputStream zos=new ZipOutputStream(outputStream);
		copyFile(zos,"ClientUpdater.jar",new File(appDir,"agentSources/ClientUpdater.jar"),true);
		zos.putNextEntry(new ZipEntry("vars"));
		PrintWriter pw=new PrintWriter(zos);
		for(ServerVariable sv:ServerVariable.all){
			if(!sv.name.equals("AGENT_VERSION")){
				pw.println(sv.serverVariableType.type+"."+sv.name+"="+sv.variable);
			}
		}
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
