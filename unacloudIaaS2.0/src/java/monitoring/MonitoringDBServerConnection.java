package monitoring;

import back.services.VariableManagerService;

import com.losandes.connectionDb.MonitorDatabaseConnection;

public class MonitoringDBServerConnection extends MonitorDatabaseConnection{

	@Override
	public void callVariables() {
//		VariableManagerService variableManagerService = new VariableManagerService();
//		ip = variableManagerService.getStringValue("MONITORING_SERVER_IP");
//	    port = variableManagerService.getIntValue("MONITORING_SERVER_PORT");
//	    name = variableManagerService.getStringValue("MONITORING_DATABASE_NAME");
//	    user = variableManagerService.getStringValue("MONITORING_DATABASE_USER");
//	    password = variableManagerService.getStringValue("MONITORING_DATABASE_PASSWORD");		
		ip = "172.24.98.119";
	    port = 27017;
	    name = "cloudMongo";
	    user = "cloudmonitoreo";
	    password = "cloudmonitoreo$#";	
	}

}
