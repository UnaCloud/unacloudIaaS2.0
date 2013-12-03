package monitoring;

import communication.messages.monitoring.MonitorReport;

public class MonitorAgent {
    void doInitialLoad(){
        MonitorDatabaseConnection.doInitialReport(MonitorReportGenerator.generateInitialReport());
    }
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