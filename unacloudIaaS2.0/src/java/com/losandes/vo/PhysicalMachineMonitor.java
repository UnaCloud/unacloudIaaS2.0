package com.losandes.vo;

import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for exposing the Physical Machine Monitor as an entity
 */
public class PhysicalMachineMonitor {
/*
    //CPU
    private String cPUModel;
    private String cPUVendor;
    private String cPUCores;
    private String cPUMhz;
    private String cPUidle;
    private String cPuUsed;
    //Memory
    private String rAMMemorySize;
    private String rAMMemoryFree;
    private String rAMMemoryUsed;
    private String swapMemorySize;
    //Hard Disk
    private String hardDiskSpace;
    private String hardDiskUsedSpace;
    private String hardDiskFreeSpace;
    private String hardDiskFileSystem;
    //Network
    private String networkInterface;
    private String networkHostname;
    private String networkIPAddress;
    private String networkMACAddress;
    private String networkNetmask;
    private String networkGateway;
    //Operating System
    private String operatingSystemName;
    private String operatingSystemVersion;
    private String operatingSystemArchitecture;
    private String operatingSystemCurrentUser;

/*    public PhysicalMachineMonitor() {
        ///CPU
        setcPUModel(NOTHING_AVAILABLE);
        setcPUVendor(NOTHING_AVAILABLE);
        setcPUCores(NOTHING_AVAILABLE);
        setcPUMhz(NOTHING_AVAILABLE);
        setcPUidle(NOTHING_AVAILABLE);
        setcPuUsed(NOTHING_AVAILABLE);
        //Memory
        setrAMMemorySize(NOTHING_AVAILABLE);
        setSwapMemorySize(NOTHING_AVAILABLE);
        setrAMMemoryFree(NOTHING_AVAILABLE);
        setrAMMemoryUsed(NOTHING_AVAILABLE);
        //Hard Disk
        setHardDiskSpace(NOTHING_AVAILABLE);
        setHardDiskFileSystem(NOTHING_AVAILABLE);
        setHardDiskUsedSpace(NOTHING_AVAILABLE);
        setHardDiskFreeSpace(NOTHING_AVAILABLE);
        //Network
        setNetworkHostname(NOTHING_AVAILABLE);
        setNetworkIPAddress(NOTHING_AVAILABLE);
        setNetworkMACAddress(NOTHING_AVAILABLE);
        setNetworkInterface(NOTHING_AVAILABLE);
        setNetworkNetmask(NOTHING_AVAILABLE);
        setNetworkGateway(NOTHING_AVAILABLE);
        //Operating System
        setOperatingSystemName(NOTHING_AVAILABLE);
        setOperatingSystemVersion(NOTHING_AVAILABLE);
        setOperatingSystemArchitecture(NOTHING_AVAILABLE);
        setOperatingSystemCurrentUser(NOTHING_AVAILABLE);
    }

/*    public void setPhysicalMachineMonitor(String clouderClientMonitor) {
        String[] clouderClientMonitorSplitted = clouderClientMonitor.split(MESSAGE_SEPARATOR_TOKEN);
        setcPUModel(clouderClientMonitorSplitted[0]);
        setcPUVendor(clouderClientMonitorSplitted[1]);
        setcPUCores(clouderClientMonitorSplitted[2]);
        setcPUMhz(clouderClientMonitorSplitted[3]);
        setcPUidle(clouderClientMonitorSplitted[4]);
        setcPuUsed(clouderClientMonitorSplitted[5]);
        //Memory
        setrAMMemorySize(clouderClientMonitorSplitted[6]);
        setSwapMemorySize(clouderClientMonitorSplitted[7]);
        setrAMMemoryFree(clouderClientMonitorSplitted[8]);
        setrAMMemoryUsed(clouderClientMonitorSplitted[9]);
        //Hard Disk
        setHardDiskSpace(clouderClientMonitorSplitted[10]);
        setHardDiskFileSystem(clouderClientMonitorSplitted[11]);
        setHardDiskUsedSpace(clouderClientMonitorSplitted[12]);
        setHardDiskFreeSpace(clouderClientMonitorSplitted[13]);
        //Network        
        setNetworkHostname(clouderClientMonitorSplitted[14]);
        setNetworkIPAddress(clouderClientMonitorSplitted[15]);
        setNetworkMACAddress(clouderClientMonitorSplitted[16]);
        setNetworkInterface(clouderClientMonitorSplitted[17]);
        setNetworkNetmask(clouderClientMonitorSplitted[18]);
        setNetworkGateway(clouderClientMonitorSplitted[19]);
        //Operating System
        setOperatingSystemName(clouderClientMonitorSplitted[20]);
        setOperatingSystemVersion(clouderClientMonitorSplitted[21]);
        setOperatingSystemArchitecture(clouderClientMonitorSplitted[22]);
        setOperatingSystemCurrentUser(clouderClientMonitorSplitted[23]);
    }

    /**
     * @return the cPUModel
     */
/*/*    public String getcPUModel() {
        return cPUModel;
    }

    /**
     * @param cPUModel the cPUModel to set
     */
/*/*    public void setcPUModel(String cPUModel) {
        this.cPUModel = cPUModel;
    }

    /**
     * @return the cPUVendor
     */
/*/*    public String getcPUVendor() {
        return cPUVendor;
    }

    /**
     * @param cPUVendor the cPUVendor to set
     */
/*/*    public void setcPUVendor(String cPUVendor) {
        this.cPUVendor = cPUVendor;
    }

    /**
     * @return the cPUCores
     */
/*/*    public String getcPUCores() {
        return cPUCores;
    }

    /**
     * @param cPUCores the cPUCores to set
     */
/*/*    public void setcPUCores(String cPUCores) {
        this.cPUCores = cPUCores;
    }

    /**
     * @return the cPUMhz
     */
/*/*    public String getcPUMhz() {
        return cPUMhz;
    }

    /**
     * @param cPUMhz the cPUMhz to set
     */
/*/*    public void setcPUMhz(String cPUMhz) {
        this.cPUMhz = cPUMhz;
    }

    /**
     * @return the cPUidle
     */
/*/*    public String getcPUidle() {
        return cPUidle;
    }

    /**
     * @param cPUidle the cPUidle to set
     */
/*/*    public void setcPUidle(String cPUidle) {
        this.cPUidle = cPUidle;
    }

    /**
     * @return the cPuUsed
     */
/*/*    public String getcPuUsed() {
        return cPuUsed;
    }

    /**
     * @param cPuUsed the cPuUsed to set
     */
/*/*    public void setcPuUsed(String cPuUsed) {
        this.cPuUsed = cPuUsed;
    }

    /**
     * @return the rAMMemorySize
     */
/*/*    public String getrAMMemorySize() {
        return rAMMemorySize;
    }

    /**
     * @param rAMMemorySize the rAMMemorySize to set
     */
/*/*    public void setrAMMemorySize(String rAMMemorySize) {
        this.rAMMemorySize = rAMMemorySize;
    }

    /**
     * @return the rAMMemoryFree
     */
/*    public String getrAMMemoryFree() {
        return rAMMemoryFree;
    }

    /**
     * @param rAMMemoryFree the rAMMemoryFree to set
     */
/*    public void setrAMMemoryFree(String rAMMemoryFree) {
        this.rAMMemoryFree = rAMMemoryFree;
    }

    /**
     * @return the rAMMemoryUsed
     */
/*    public String getrAMMemoryUsed() {
        return rAMMemoryUsed;
    }

    /**
     * @param rAMMemoryUsed the rAMMemoryUsed to set
     */
/*    public void setrAMMemoryUsed(String rAMMemoryUsed) {
        this.rAMMemoryUsed = rAMMemoryUsed;
    }

    /**
     * @return the swapMemorySize
     */
/*    public String getSwapMemorySize() {
        return swapMemorySize;
    }

    /**
     * @param swapMemorySize the swapMemorySize to set
     */
/*    public void setSwapMemorySize(String swapMemorySize) {
        this.swapMemorySize = swapMemorySize;
    }

    /**
     * @return the hardDiskSpace
     */
/*    public String getHardDiskSpace() {
        return hardDiskSpace;
    }

    /**
     * @param hardDiskSpace the hardDiskSpace to set
     */
/*    public void setHardDiskSpace(String hardDiskSpace) {
        this.hardDiskSpace = hardDiskSpace;
    }

    /**
     * @return the hardDiskUsedSpace
     */
/*    public String getHardDiskUsedSpace() {
        return hardDiskUsedSpace;
    }

    /**
     * @param hardDiskUsedSpace the hardDiskUsedSpace to set
     */
/*    public void setHardDiskUsedSpace(String hardDiskUsedSpace) {
        this.hardDiskUsedSpace = hardDiskUsedSpace;
    }

    /**
     * @return the hardDiskFreeSpace
     */
/*    public String getHardDiskFreeSpace() {
        return hardDiskFreeSpace;
    }

    /**
     * @param hardDiskFreeSpace the hardDiskFreeSpace to set
     */
/*    public void setHardDiskFreeSpace(String hardDiskFreeSpace) {
        this.hardDiskFreeSpace = hardDiskFreeSpace;
    }

    /**
     * @return the hardDiskFileSystem
     */
/*    public String getHardDiskFileSystem() {
        return hardDiskFileSystem;
    }

    /**
     * @param hardDiskFileSystem the hardDiskFileSystem to set
     */
/*    public void setHardDiskFileSystem(String hardDiskFileSystem) {
        this.hardDiskFileSystem = hardDiskFileSystem;
    }

    /**
     * @return the networkInterface
     */
/*    public String getNetworkInterface() {
        return networkInterface;
    }

    /**
     * @param networkInterface the networkInterface to set
     */
/*    public void setNetworkInterface(String networkInterface) {
        this.networkInterface = networkInterface;
    }

    /**
     * @return the networkHostname
     */
/*    public String getNetworkHostname() {
        return networkHostname;
    }

    /**
     * @param networkHostname the networkHostname to set
     */
/*    public void setNetworkHostname(String networkHostname) {
        this.networkHostname = networkHostname;
    }

    /**
     * @return the networkIPAddress
     */
/*    public String getNetworkIPAddress() {
        return networkIPAddress;
    }

    /**
     * @param networkIPAddress the networkIPAddress to set
     */
/*    public void setNetworkIPAddress(String networkIPAddress) {
        this.networkIPAddress = networkIPAddress;
    }

    /**
     * @return the networkMACAddress
     */
/*    public String getNetworkMACAddress() {
        return networkMACAddress;
    }

    /**
     * @param networkMACAddress the networkMACAddress to set
     */
/*    public void setNetworkMACAddress(String networkMACAddress) {
        this.networkMACAddress = networkMACAddress;
    }

    /**
     * @return the networkNetmask
     */
/*    public String getNetworkNetmask() {
        return networkNetmask;
    }

    /**
     * @param networkNetmask the networkNetmask to set
     */
/*    public void setNetworkNetmask(String networkNetmask) {
        this.networkNetmask = networkNetmask;
    }

    /**
     * @return the networkGateway
     */
/*    public String getNetworkGateway() {
        return networkGateway;
    }

    /**
     * @param networkGateway the networkGateway to set
     */
/*    public void setNetworkGateway(String networkGateway) {
        this.networkGateway = networkGateway;
    }

    /**
     * @return the operatingSystemName
     */
/*    public String getOperatingSystemName() {
        return operatingSystemName;
    }

    /**
     * @param operatingSystemName the operatingSystemName to set
     */
/*    public void setOperatingSystemName(String operatingSystemName) {
        this.operatingSystemName = operatingSystemName;
    }

    /**
     * @return the operatingSystemVersion
     */
/*    public String getOperatingSystemVersion() {
        return operatingSystemVersion;
    }

    /**
     * @param operatingSystemVersion the operatingSystemVersion to set
     */
/*    public void setOperatingSystemVersion(String operatingSystemVersion) {
        this.operatingSystemVersion = operatingSystemVersion;
    }

    /**
     * @return the operatingSystemArchitecture
     */
/*    public String getOperatingSystemArchitecture() {
        return operatingSystemArchitecture;
    }

    /**
     * @param operatingSystemArchitecture the operatingSystemArchitecture to set
     */
/*    public void setOperatingSystemArchitecture(String operatingSystemArchitecture) {
        this.operatingSystemArchitecture = operatingSystemArchitecture;
    }

    /**
     * @return the operatingSystemCurrentUser
     */
/*    public String getOperatingSystemCurrentUser() {
        return operatingSystemCurrentUser;
    }

    /**
     * @param operatingSystemCurrentUser the operatingSystemCurrentUser to set
     */
/*    public void setOperatingSystemCurrentUser(String operatingSystemCurrentUser) {
        this.operatingSystemCurrentUser = operatingSystemCurrentUser;
    }*/
}

    
