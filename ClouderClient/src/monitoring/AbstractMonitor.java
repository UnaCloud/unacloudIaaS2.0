package monitoring;

import unacloudEnums.MonitoringStatus;

/** 
 * @author Cesar
 * This class is the template for monitoring classes. There are three main task for all monitoring process; initial, monitoring, final
 * @param frecuency check frequency in seconds
 * @param windowSizeTime window check time size in milliseconds
 */
public abstract class AbstractMonitor implements Runnable{

	/**
	 * check frequency in seconds
	 */
	protected int frecuency;
	/**
	 * window check time size in seconds
	 */
	protected int windowSizeTime;
	
	/**
	 * file path to record data
	 */
	protected String recordPath;
	
	/**
	 * Time to reduces the first execution
	 */
	protected long reduce;
	/**
	 * Status of monitoring process; Check MonitoringStatus Enum to more info
	 */
	protected MonitoringStatus status;
	/**
	 * Connection to DB from Agent
	 */
	protected MonitorDBAgentConnection connection;
	
	public AbstractMonitor(String record) throws Exception {
		if(record==null)throw new Exception("record path is missing");
		recordPath=record;
		status = MonitoringStatus.DISABLE;
		connection = new MonitorDBAgentConnection();
	}
	
	@Override
	public void run() {
		if(status==MonitoringStatus.INIT)
		try {
			if(recordPath!=null){
				status = MonitoringStatus.RUNNING;
				System.out.println("Lets work for "+windowSizeTime);
				doMonitoring();
				doFinal();
				if(status==MonitoringStatus.RUNNING)status=MonitoringStatus.INIT;
			}		
		} catch (Exception e) {
			e.printStackTrace();
			sendError(e);
		}		
	}
	public void stopMonitor(){
		if(status==MonitoringStatus.RUNNING)status = MonitoringStatus.STOPPED;
		else if(status==MonitoringStatus.ERROR)status = MonitoringStatus.OFF;
	}
	public void turnOff(){
		if(status!=MonitoringStatus.DISABLE)status=MonitoringStatus.OFF;
	}
	/**
     * Do initial task to control monitoring
     * @throws Exception 
     */
	protected abstract void doInitial() throws Exception;
	/**
	 * Starts monitoring process
	**/
	protected abstract void doMonitoring() throws Exception;
	/**
	 * Record data in Database if it is necessary
	 * @throws Exception 
	 */
	protected abstract void doFinal() throws Exception;
	/**
	 * Unified send error method to communicate when process has failed
	 */
	protected abstract void sendError(Exception e);
	/**
	 * check frequency in seconds
	 */
	public int getFrecuency() {
		return frecuency;
	}
	/**
	 * check frequency in seconds
	 * @param frecuency
	 */
	public void setFrecuency(int frecuency) {
		this.frecuency = frecuency;
	}
	/** 
	 * @return size time in seconds
	 */
	public int getWindowSizeTime() {
		return windowSizeTime;
	}
	/** 
	 * @param windowSizeTime: size time in seconds
	 */
	public void setWindowSizeTime(int windowSizeTime) {
		this.windowSizeTime = windowSizeTime;
	}
	public long getReduce() {
		return reduce;
	}
	public void setReduce(long reduce) {
		this.reduce = reduce;
	}
	public String getRecordPath() {
		return recordPath;
	}
	public void setRecordPath(String recordPath) {
		this.recordPath = recordPath;
	}
	public MonitoringStatus getStatus() {
		return status;
	}
	public void setStatus(MonitoringStatus status) {
		this.status = status;
	}
	public boolean isRunning(){
		return status==MonitoringStatus.RUNNING;
	}
	public boolean isStopped(){
		return status==MonitoringStatus.STOPPED;
	}
	public boolean isError(){
		return status==MonitoringStatus.ERROR;
	}
	public boolean isDisable(){
		return status==MonitoringStatus.DISABLE;
	}
	public boolean isReady(){
		return status==MonitoringStatus.INIT;
	}
}
