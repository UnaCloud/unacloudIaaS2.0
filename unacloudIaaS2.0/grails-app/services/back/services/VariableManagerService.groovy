package back.services

import unacloud2.ServerVariable;

class VariableManagerService {
	//TODO crear un cache para rendimiento0
    def String getStringValue(String key){
		ServerVariable sv=ServerVariable.findByName(key);
		if(sv==null)return null;
		return sv.getVariable();
	}
	def int getIntValue(String key){
		ServerVariable sv=ServerVariable.findByName(key);
		if(sv==null||sv.getVariable()==null||!sv.getVariable().matches("[0-9]+"))return 0;
		return Integer.parseInt(sv.getVariable());
	}
	
	def updateAgentVersion(){
		ServerVariable agentVersion= ServerVariable.findByName("AGENT_VERSION")
		int newVerNumber= ((agentVersion.getVariable()-"2.0.") as Integer)+1
		String newVersion=  "2.0."+ newVerNumber
		agentVersion.putAt("variable", newVersion)
	}
}
