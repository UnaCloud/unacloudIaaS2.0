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
	private MonitoringStatus status;
	/**
	 * Connection to DB from Agent
	 */
	protected MonitorDBAgentConnection connection;
	
	protected AbstractMonitor() throws Exception {		
		status = MonitoringStatus.DISABLE;
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
	public void toStop(){
		if(status==MonitoringStatus.RUNNING)status = MonitoringStatus.STOPPED;
		else if(status==MonitoringStatus.ERROR)status = MonitoringStatus.OFF;
	}
	public void toEnable(String record)throws Exception{
		if(!isDisable())return;
		if(record==null)throw new Exception("record path is missing");
		recordPath=record;
		this.status = MonitoringStatus.OFF;
		connection = new MonitorDBAgentConnection();
	}
	public void offToInit(int frecuency, int window, int time){
		if(!isOff())return;
		if(updateVariables(frecuency, window, time))		
		    status = MonitoringStatus.INIT;		
	}
	public void toDisable(){
		if(isOff())status = MonitoringStatus.DISABLE;
	}
	public void toOff(){
		if(isStopped())status = MonitoringStatus.OFF;
	}
	protected void toError() {
		this.status = MonitoringStatus.ERROR;
	}
	public boolean updateVariables(int frecuency, int window, int time){
		if(frecuency>0&&window>0&&frecuency<window){	
			this.frecuency = frecuency; 
		    this.windowSizeTime = window;
			this.reduce = time;
		    return true;		
		}	
		System.out.println("Frecuency is greater than window");
		return false;
	}
	public void setRecordPath(String recordPath) {
		this.recordPath = recordPath;
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
	protected int getFrecuency() {
		return frecuency;
	}
	/** 
	 * @return size time in seconds
	 */
	protected int getWindowSizeTime() {
		return windowSizeTime;
	}
	protected long getReduce() {
		return reduce;
	}
	protected String getRecordPath() {
		return recordPath;
	}
	protected boolean isRunning(){
		return status==MonitoringStatus.RUNNING;
	}
	protected boolean isStopped(){
		return status==MonitoringStatus.STOPPED;
	}
	protected boolean isError(){
		return status==MonitoringStatus.ERROR;
	}
	protected boolean isDisable(){
		return status==MonitoringStatus.DISABLE;
	}
	protected boolean isReady(){
		return status==MonitoringStatus.INIT;
	}
	protected boolean isOff(){
		return status==MonitoringStatus.OFF;
	}
	
	public MonitoringStatus getStatus(){
		return status;
	}
}
