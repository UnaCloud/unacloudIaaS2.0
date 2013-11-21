package communication.messages.monitoring;

import java.sql.Timestamp;

public class MonitorReport{

	String UUID;
	Timestamp timest;
	int ContadorRegistros;
	String UserName;
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
	public MonitorReport(String uUID, Timestamp timest, int contadorRegistros, String userName, double uptime, double mflops, double timeinSecs, double idle, double d, double cPuser, double sys, double nice, double wait, double combined, long user, long sys0, long nice0, long wait0, long idle0, float rAMMemoryFree, float rAMMemoryUsed, double freePercent, double usedPercent, float swapMemoryFree, float swapMemoryPageIn, float swapMemoryPageOut, float swapMemoryUsed, long hardDiskFreeSpace, long hardDiskUsedSpace, String networkIPAddress, String networkInterface, String networkNetmask, String networkGateway, long rxBytes, long txBytes, long speed, long rxErrors, long txErrors, long rxPackets, long txPackets,String processes){
		super();
		UUID = uUID;
		this.timest = timest;
		ContadorRegistros = contadorRegistros;
		UserName = userName;
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
        
}
