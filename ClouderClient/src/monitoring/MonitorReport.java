package monitoring;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MonitorReport{

	String UUID;
	Timestamp timest;
	int ContadorRegistros;
	String UserName, hostName;
	double uptime,mflops,timeinSecs,idle,d,CPuser,sys,nice,wait,combined;
	long user,sys0,nice0,wait0,idle0;
	float rAMMemoryFree,rAMMemoryUsed;
	double freePercent,usedPercent;
	float swapMemoryFree,swapMemoryPageIn,swapMemoryPageOut,swapMemoryUsed;
	long hardDiskFreeSpace;
	long hardDiskUsedSpace;
	String networkIPAddress;
	String networkInterface;
	String networkNetmask;
	String networkGateway;
	long rxBytes;
	long txBytes;
	long speed;
	long rxErrors;
	long txErrors;
	long rxPackets;
	long txPackets;
    String processes;
    
	public MonitorReport(String uUID, Timestamp timest, int contadorRegistros, String userName, String host , double uptime, double mflops, double timeinSecs, double idle, double d, double cPuser, double sys, double nice, double wait, double combined, long user, long sys0, long nice0, long wait0, long idle0, float rAMMemoryFree, float rAMMemoryUsed, double freePercent, double usedPercent, float swapMemoryFree, float swapMemoryPageIn, float swapMemoryPageOut, float swapMemoryUsed, long hardDiskFreeSpace, long hardDiskUsedSpace, String networkIPAddress, String networkInterface, String networkNetmask, String networkGateway, long rxBytes, long txBytes, long speed, long rxErrors, long txErrors, long rxPackets, long txPackets,String processes){
		super();
		UUID = uUID;
		this.timest = timest;
		ContadorRegistros = contadorRegistros;
		UserName = userName;
		hostName = host;
		this.uptime = uptime;
		this.mflops = mflops;
		this.timeinSecs = timeinSecs;
		this.idle = idle;
		this.d = d;
		CPuser = cPuser;
		this.sys = sys;
		this.nice = nice;
		this.wait = wait;
		this.combined = combined;
		this.user = user;
		this.sys0 = sys0;
		this.nice0 = nice0;
		this.wait0 = wait0;
		this.idle0 = idle0;
		this.rAMMemoryFree = rAMMemoryFree;
		this.rAMMemoryUsed = rAMMemoryUsed;
		this.freePercent = freePercent;
		this.usedPercent = usedPercent;
		this.swapMemoryFree = swapMemoryFree;
		this.swapMemoryPageIn = swapMemoryPageIn;
		this.swapMemoryPageOut = swapMemoryPageOut;
		this.swapMemoryUsed = swapMemoryUsed;
		this.hardDiskFreeSpace = hardDiskFreeSpace;
		this.hardDiskUsedSpace = hardDiskUsedSpace;
		this.networkIPAddress = networkIPAddress;
		this.networkInterface = networkInterface;
		this.networkNetmask = networkNetmask;
		this.networkGateway = networkGateway;
		this.rxBytes = rxBytes;
		this.txBytes = txBytes;
		this.speed = speed;
		this.rxErrors = rxErrors;
		this.txErrors = txErrors;
		this.rxPackets = rxPackets;
		this.txPackets = txPackets;
        this.processes=processes;
	}
	
	public MonitorReport(String line) throws ParseException {		
			
		line = line.replace("MonitorReport [", "").replace("]", "");
		processes = line.substring(line.indexOf("{")+1, line.indexOf("}"));
		line = line.substring(0,line.indexOf(", processes"));
    	String [] elements = line.split(",");
    	for (String elem : elements) {
			String [] components = elem.split("=");			
			if(components[0].trim().startsWith("ContadorRegistros"))ContadorRegistros = Integer.parseInt(components[1]);
			else if(components[0].trim().startsWith("timest")){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
			    Date parsedDate = dateFormat.parse(components[1]);
			    timest = new java.sql.Timestamp(parsedDate.getTime());
			}
			else if(components[0].trim().startsWith("UserName")) UserName = components[1];
			else if(components[0].trim().startsWith("hostName")) hostName = components[1];
			else if(components[0].trim().startsWith("uptime")) uptime = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("mflops")) mflops = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("timeinSecs")) timeinSecs = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("idle")) idle = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("d")) d = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("CPuser")) CPuser = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("sys")) sys = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("nice")) nice = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("wait")) wait = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("combined")) combined = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("user")) user = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("sys0")) sys0 = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("nice0")) nice0 = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("wait0")) wait0 = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("idle0")) idle0 = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("rAMMemoryFree")) rAMMemoryFree = Float.parseFloat(components[1]);
			else if(components[0].trim().startsWith("rAMMemoryUsed")) rAMMemoryUsed = Float.parseFloat(components[1]);
			else if(components[0].trim().startsWith("freePercent")) freePercent = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("usedPercent")) usedPercent = Double.parseDouble(components[1]);
			else if(components[0].trim().startsWith("swapMemoryFree")) swapMemoryFree = Float.parseFloat(components[1]);
			else if(components[0].trim().startsWith("swapMemoryPageIn")) swapMemoryPageIn = Float.parseFloat(components[1]);
			else if(components[0].trim().startsWith("swapMemoryPageOut")) swapMemoryPageOut = Float.parseFloat(components[1]);
			else if(components[0].trim().startsWith("swapMemoryUsed")) swapMemoryUsed = Float.parseFloat(components[1]);
			else if(components[0].trim().startsWith("hardDiskFreeSpace")) hardDiskFreeSpace = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("hardDiskUsedSpace")) hardDiskUsedSpace = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("networkIPAddress")) networkIPAddress = components[1];
			else if(components[0].trim().startsWith("networkInterface")) networkInterface = components[1];
			else if(components[0].trim().startsWith("networkNetmask")) networkNetmask = components[1];
			else if(components[0].trim().startsWith("networkGateway")) networkGateway = components[1];
			else if(components[0].trim().startsWith("rxBytes")) rxBytes = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("txBytes")) txBytes = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("speed")) speed = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("rxErrors")) rxErrors = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("txErrors")) txErrors = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("rxPackets")) rxPackets = Long.parseLong(components[1]);
			else if(components[0].trim().startsWith("txPackets")) txPackets = Long.parseLong(components[1]);
		}
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public Timestamp getTimest() {
		return timest;
	}

	public void setTimest(Timestamp timest) {
		this.timest = timest;
	}

	public int getContadorRegistros() {
		return ContadorRegistros;
	}

	public void setContadorRegistros(int contadorRegistros) {
		ContadorRegistros = contadorRegistros;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public double getUptime() {
		return uptime;
	}

	public void setUptime(double uptime) {
		this.uptime = uptime;
	}

	public double getMflops() {
		return mflops;
	}

	public void setMflops(double mflops) {
		this.mflops = mflops;
	}

	public double getTimeinSecs() {
		return timeinSecs;
	}

	public void setTimeinSecs(double timeinSecs) {
		this.timeinSecs = timeinSecs;
	}

	public double getIdle() {
		return idle;
	}

	public void setIdle(double idle) {
		this.idle = idle;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public double getCPuser() {
		return CPuser;
	}

	public void setCPuser(double cPuser) {
		CPuser = cPuser;
	}

	public double getSys() {
		return sys;
	}

	public void setSys(double sys) {
		this.sys = sys;
	}

	public double getNice() {
		return nice;
	}

	public void setNice(double nice) {
		this.nice = nice;
	}

	public double getWait() {
		return wait;
	}

	public void setWait(double wait) {
		this.wait = wait;
	}

	public double getCombined() {
		return combined;
	}

	public void setCombined(double combined) {
		this.combined = combined;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public long getSys0() {
		return sys0;
	}

	public void setSys0(long sys0) {
		this.sys0 = sys0;
	}

	public long getNice0() {
		return nice0;
	}

	public void setNice0(long nice0) {
		this.nice0 = nice0;
	}

	public long getWait0() {
		return wait0;
	}

	public void setWait0(long wait0) {
		this.wait0 = wait0;
	}

	public long getIdle0() {
		return idle0;
	}

	public void setIdle0(long idle0) {
		this.idle0 = idle0;
	}

	public float getrAMMemoryFree() {
		return rAMMemoryFree;
	}

	public void setrAMMemoryFree(float rAMMemoryFree) {
		this.rAMMemoryFree = rAMMemoryFree;
	}

	public float getrAMMemoryUsed() {
		return rAMMemoryUsed;
	}

	public void setrAMMemoryUsed(float rAMMemoryUsed) {
		this.rAMMemoryUsed = rAMMemoryUsed;
	}

	public double getFreePercent() {
		return freePercent;
	}

	public void setFreePercent(double freePercent) {
		this.freePercent = freePercent;
	}

	public double getUsedPercent() {
		return usedPercent;
	}

	public void setUsedPercent(double usedPercent) {
		this.usedPercent = usedPercent;
	}

	public float getSwapMemoryFree() {
		return swapMemoryFree;
	}

	public void setSwapMemoryFree(float swapMemoryFree) {
		this.swapMemoryFree = swapMemoryFree;
	}

	public float getSwapMemoryPageIn() {
		return swapMemoryPageIn;
	}

	public void setSwapMemoryPageIn(float swapMemoryPageIn) {
		this.swapMemoryPageIn = swapMemoryPageIn;
	}

	public float getSwapMemoryPageOut() {
		return swapMemoryPageOut;
	}

	public void setSwapMemoryPageOut(float swapMemoryPageOut) {
		this.swapMemoryPageOut = swapMemoryPageOut;
	}

	public float getSwapMemoryUsed() {
		return swapMemoryUsed;
	}

	public void setSwapMemoryUsed(float swapMemoryUsed) {
		this.swapMemoryUsed = swapMemoryUsed;
	}

	public long getHardDiskFreeSpace() {
		return hardDiskFreeSpace;
	}

	public void setHardDiskFreeSpace(long hardDiskFreeSpace) {
		this.hardDiskFreeSpace = hardDiskFreeSpace;
	}

	public long getHardDiskUsedSpace() {
		return hardDiskUsedSpace;
	}

	public void setHardDiskUsedSpace(long hardDiskUsedSpace) {
		this.hardDiskUsedSpace = hardDiskUsedSpace;
	}

	public String getNetworkIPAddress() {
		return networkIPAddress;
	}

	public void setNetworkIPAddress(String networkIPAddress) {
		this.networkIPAddress = networkIPAddress;
	}

	public String getNetworkInterface() {
		return networkInterface;
	}

	public void setNetworkInterface(String networkInterface) {
		this.networkInterface = networkInterface;
	}

	public String getNetworkNetmask() {
		return networkNetmask;
	}

	public void setNetworkNetmask(String networkNetmask) {
		this.networkNetmask = networkNetmask;
	}

	public String getNetworkGateway() {
		return networkGateway;
	}

	public void setNetworkGateway(String networkGateway) {
		this.networkGateway = networkGateway;
	}

	public long getRxBytes() {
		return rxBytes;
	}

	public void setRxBytes(long rxBytes) {
		this.rxBytes = rxBytes;
	}

	public long getTxBytes() {
		return txBytes;
	}

	public void setTxBytes(long txBytes) {
		this.txBytes = txBytes;
	}

	public long getSpeed() {
		return speed;
	}

	public void setSpeed(long speed) {
		this.speed = speed;
	}

	public long getRxErrors() {
		return rxErrors;
	}

	public void setRxErrors(long rxErrors) {
		this.rxErrors = rxErrors;
	}

	public long getTxErrors() {
		return txErrors;
	}

	public void setTxErrors(long txErrors) {
		this.txErrors = txErrors;
	}

	public long getRxPackets() {
		return rxPackets;
	}

	public void setRxPackets(long rxPackets) {
		this.rxPackets = rxPackets;
	}

	public long getTxPackets() {
		return txPackets;
	}

	public void setTxPackets(long txPackets) {
		this.txPackets = txPackets;
	}

    public String getProcesses() {
        return processes;
    }
    
    public String getHostName() {
		return hostName;
	}
    
    public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public String toString() {
		return "MonitorReport [ContadorRegistros=" + ContadorRegistros + ", timest=" + timest
				+ ", UserName="
				+ UserName + ", hostName="+ hostName+", uptime=" + uptime + ", mflops=" + mflops
				+ ", timeinSecs=" + timeinSecs + ", idle=" + idle + ", d=" + d
				+ ", CPuser=" + CPuser + ", sys=" + sys + ", nice=" + nice
				+ ", wait=" + wait + ", combined=" + combined + ", user="
				+ user + ", sys0=" + sys0 + ", nice0=" + nice0 + ", wait0="
				+ wait0 + ", idle0=" + idle0 + ", rAMMemoryFree="
				+ rAMMemoryFree + ", rAMMemoryUsed=" + rAMMemoryUsed
				+ ", freePercent=" + freePercent + ", usedPercent="
				+ usedPercent + ", swapMemoryFree=" + swapMemoryFree
				+ ", swapMemoryPageIn=" + swapMemoryPageIn
				+ ", swapMemoryPageOut=" + swapMemoryPageOut
				+ ", swapMemoryUsed=" + swapMemoryUsed + ", hardDiskFreeSpace="
				+ hardDiskFreeSpace + ", hardDiskUsedSpace="
				+ hardDiskUsedSpace + ", networkIPAddress=" + networkIPAddress
				+ ", networkInterface=" + networkInterface
				+ ", networkNetmask=" + networkNetmask + ", networkGateway="
				+ networkGateway + ", rxBytes=" + rxBytes + ", txBytes="
				+ txBytes + ", speed=" + speed + ", rxErrors=" + rxErrors
				+ ", txErrors=" + txErrors + ", rxPackets=" + rxPackets
				+ ", txPackets=" + txPackets + ", processes={" + processes + "}]";
	}
    
        
}
