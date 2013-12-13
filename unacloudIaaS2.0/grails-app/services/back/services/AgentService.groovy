package back.services

import communication.messages.ao.UpdateAgentMessage;

import javassist.bytecode.stackmap.BasicBlock.Catch;
import unacloud2.PhysicalMachine;

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
}
