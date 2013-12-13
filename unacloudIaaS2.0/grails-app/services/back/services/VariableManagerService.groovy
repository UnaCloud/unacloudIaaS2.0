package back.services

import unacloud2.ServerVariable;

class VariableManagerService {
	//TODO crear un cache para rendimiento0
    def String getStringValue(String key){
		ServerVariable sv=ServerVariable.findBy(variable:key);
		if(sv==null)return null;
		return sv.getVariable();
	}
	def int getIntValue(String key){
		ServerVariable sv=ServerVariable.findBy(variable:key);
		if(sv==null||sv.getVariable()==null||!sv.getVariable().matches("[0-9]+"))return 0;
		return Integer.parseInt(sv.getVariable());
	}
}
