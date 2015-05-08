package unacloud2

import monitoring.MonitorResources;
import grails.transaction.Transactional

@Transactional
class MonitoringService {

	MonitorResources monitor = new MonitorResources();
	
    def getMetricsCPU(String host){
		return monitor.getCPUMetrics(host);
	}
	
	def generateEnergyReport(String host, Date start, Date finish){
		
	}
}
