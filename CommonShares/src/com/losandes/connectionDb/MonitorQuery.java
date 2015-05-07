package com.losandes.connectionDb;

import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class MonitorQuery {
	
	private MonitorDatabaseConnection connection;
	
	public MonitorQuery(MonitorDatabaseConnection c) {
		this.connection = c;
	}
	
	public void getEnergyReportsByDate(Date start, Date end, String pm) throws UnknownHostException{
		MongoConnection m = connection.generateConnection();
		BasicDBObject query = new BasicDBObject("Hostname", "ISC208");
		DBCursor cursor = m.energyCollection().find(query);
		try {
			while(cursor.hasNext()) {
			BasicDBObject db = (BasicDBObject) cursor.next();
			System.out.println(db);
				   System.out.println(db.getString("_id"));
				   System.out.println(db.getObjectId("_id").getTimestamp()+" --- "+new Date(db.getObjectId("_id").getTime()));
			   }
		} finally {
			 cursor.close();
		}
	}
	
	public void getCpuReportsByDate(Date start, Date end, String pm){
		
	}

}
