package monitoring;

import communication.messages.monitoring.MonitorReport;

public class MonitorAgent {
    /**
     * Orders to make initial report with generated information
     */
	void doInitialLoad(){
        MonitorDatabaseConnection.doInitialReport(MonitorReportGenerator.generateInitialReport());
    }
	
	/**
	 * Starts monitoring process
	 * @param frecuency check frequency in seconds
	 * @param windowSize window check time size
	 * @throws InterruptedException
	 */
    public void startMonitoring(int frecuency, int windowSize) throws InterruptedException{
        if (frecuency < 10)frecuency = 10;
        if(windowSize<2)windowSize=2;
        if((windowSize*frecuency)/60>60)windowSize=60;
        frecuency *= 1000;
        MonitorReport[] statusReports = new MonitorReport[windowSize];
        for (int i = 0; i < statusReports.length; i++) {
            statusReports[i]=MonitorReportGenerator.generateStateReport();
            Thread.sleep(frecuency);
        }
        MonitorDatabaseConnection.doBatchReport(statusReports);
    }
}