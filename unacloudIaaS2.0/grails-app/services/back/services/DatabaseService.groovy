package back.services

import groovy.sql.Sql

class DatabaseService {

	//GRANT EVENT ON iaas2dev.* TO 'iaasdev'@'%';
	def dataSource
	def initDatabase(){
		def sql = new Sql(dataSource)
		sql.execute 'create event if not exists updatePMs ON SCHEDULE EVERY 2 MINUTE STARTS CURRENT_TIMESTAMP DO update physical_machine set state = \'OFF\' where CURRENT_TIMESTAMP > DATE_ADD(last_report, INTERVAL 4 MINUTE);'
		sql.execute 'update physical_machine set state = \'OFF\', with_user=0;'
		sql.close();
	}
}
