package monitoring;

import java.net.UnknownHostException;
import java.util.Date;

import com.losandes.connectionDb.MongoConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MonitorQuery {
	
	private MonitoringDBServerConnection connection;
	
	public MonitorQuery() throws UnknownHostException {
		connection = new MonitoringDBServerConnection();
		start();
	}
	
	public static void main(String[] args) {
		try {
			new MonitorQuery();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start() throws UnknownHostException{
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

}
