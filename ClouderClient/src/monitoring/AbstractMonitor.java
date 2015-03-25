package monitoring;

/** 
 * @author Cesar
 * This class is the template for monitoring classes. There are three main task for all monitoring process; initial, monitoring, final
 * @param frecuency check frequency in seconds
 * @param windowSizeTime window check time size in milliseconds
 */
public abstract class AbstractMonitor extends Thread{

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
	
	
	@Override
	public void run() {
		try {
			if(recordPath!=null){
				doInitial();
				doMonitoring();
				doFinal();
			}		
		} catch (Exception e) {
			e.printStackTrace();
			sendError(e);
		}
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
}
