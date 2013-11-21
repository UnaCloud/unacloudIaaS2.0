package communication.messages.monitoring;

import communication.UnaCloudAbstractMessage;
import communication.UnaCloudMessage;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class provides the attributes to encapsulate the result of a monitor
 * test. Among others, it includes cpu ussage, memory ussage and other
 * statistics
 *
 * @author GSot
 *
 */
public class MonitorInitialReport extends UnaCloudAbstractMessage implements Serializable{

	private static final long serialVersionUID = -2738566841932331498L;
	private String UUID;
    private Timestamp timest;
    private String hostname;
    private String operatingSystemName;
    private String operatingSystemVersion;
    private String operatingSystemArchitect;
    private String cPUModel;
    private String cPUVendor;
    private int cPUCores;
    private int totalSockets;
    private String cPUMhz;
    private int coresPerSocket;
    private float rAMMemorySize;
    private float swapMemorySize;
    private long hardDiskSpace;
    private String hardDiskFileSystem;
    private String networkMACAddress;

    public MonitorInitialReport(String uUID, Timestamp timest, String hostname, String operatingSystemName, String operatingSystemVersion, String operatingSystemArchitect, String cPUModel, String cPUVendor, int cPUCores, int totalSockets, String cPUMhz, int coresPerSocket, float rAMMemorySize, float swapMemorySize, long hardDiskSpace, String hardDiskFileSystem, String networkMACAddress) {
        super(REGISTRATION_OPERATION,0);
        UUID = uUID;
        this.timest = timest;
        this.hostname = hostname;
        this.operatingSystemName = operatingSystemName;
        this.operatingSystemVersion = operatingSystemVersion;
        this.operatingSystemArchitect = operatingSystemArchitect;
        this.cPUModel = cPUModel;
        this.cPUVendor = cPUVendor;
        this.cPUCores = cPUCores;
        this.totalSockets = totalSockets;
        this.cPUMhz = cPUMhz;
        this.coresPerSocket = coresPerSocket;
        this.rAMMemorySize = rAMMemorySize;
        this.swapMemorySize = swapMemorySize;
        this.hardDiskSpace = hardDiskSpace;
        this.hardDiskFileSystem = hardDiskFileSystem;
        this.networkMACAddress = networkMACAddress;
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

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getOperatingSystemName() {
        return operatingSystemName;
    }

    public void setOperatingSystemName(String operatingSystemName) {
        this.operatingSystemName = operatingSystemName;
    }

    public String getOperatingSystemVersion() {
        return operatingSystemVersion;
    }

    public void setOperatingSystemVersion(String operatingSystemVersion) {
        this.operatingSystemVersion = operatingSystemVersion;
    }

    public String getOperatingSystemArchitect() {
        return operatingSystemArchitect;
    }

    public void setOperatingSystemArchitect(String operatingSystemArchitect) {
        this.operatingSystemArchitect = operatingSystemArchitect;
    }

    public String getcPUModel() {
        return cPUModel;
    }

    public void setcPUModel(String cPUModel) {
        this.cPUModel = cPUModel;
    }

    public String getcPUVendor() {
        return cPUVendor;
    }

    public void setcPUVendor(String cPUVendor) {
        this.cPUVendor = cPUVendor;
    }

    public int getcPUCores() {
        return cPUCores;
    }

    public void setcPUCores(int cPUCores) {
        this.cPUCores = cPUCores;
    }

    public int getTotalSockets() {
        return totalSockets;
    }

    public void setTotalSockets(int totalSockets) {
        this.totalSockets = totalSockets;
    }

    public String getcPUMhz() {
        return cPUMhz;
    }

    public void setcPUMhz(String cPUMhz) {
        this.cPUMhz = cPUMhz;
    }

    public int getCoresPerSocket() {
        return coresPerSocket;
    }

    public void setCoresPerSocket(int coresPerSocket) {
        this.coresPerSocket = coresPerSocket;
    }

    public float getrAMMemorySize() {
        return rAMMemorySize;
    }

    public void setrAMMemorySize(float rAMMemorySize) {
        this.rAMMemorySize = rAMMemorySize;
    }

    public float getSwapMemorySize() {
        return swapMemorySize;
    }

    public void setSwapMemorySize(float swapMemorySize) {
        this.swapMemorySize = swapMemorySize;
    }

    public long getHardDiskSpace() {
        return hardDiskSpace;
    }

    public void setHardDiskSpace(long hardDiskSpace) {
        this.hardDiskSpace = hardDiskSpace;
    }

    public String getHardDiskFileSystem() {
        return hardDiskFileSystem;
    }

    public void setHardDiskFileSystem(String hardDiskFileSystem) {
        this.hardDiskFileSystem = hardDiskFileSystem;
    }

    public String getNetworkMACAddress() {
        return networkMACAddress;
    }

    public void setNetworkMACAddress(String networkMACAddress) {
        this.networkMACAddress = networkMACAddress;
    }

    
    @Override
    public String toString() {
        return "MonitorInitialReport{" + "UUID=" + UUID + ", timest=" + timest + ", hostname=" + hostname + ", operatingSystemName=" + operatingSystemName + ", operatingSystemVersion=" + operatingSystemVersion + ", operatingSystemArchitect=" + operatingSystemArchitect + ", cPUModel=" + cPUModel + ", cPUVendor=" + cPUVendor + ", cPUCores=" + cPUCores + ", totalSockets=" + totalSockets + ", cPUMhz=" + cPUMhz + ", coresPerSocket=" + coresPerSocket + ", rAMMemorySize=" + rAMMemorySize + ", swapMemorySize=" + swapMemorySize + ", hardDiskSpace=" + hardDiskSpace + ", hardDiskFileSystem=" + hardDiskFileSystem + ", networkMACAddress=" + networkMACAddress + '}';
    }

    @Override
    public void processMessage(UnaCloudMessage message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
