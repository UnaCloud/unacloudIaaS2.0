package com.losandes.persistence.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Physical Machine entity
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "PhysicalMachine.findByPhysicalMachineName", query = "SELECT pm FROM PhysicalMachine pm WHERE pm.physicalMachineName = :physicalMachineName")})
public class PhysicalMachine implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    private String physicalMachineName;
    private String physicalMachineUser;
    private String physicalMachineIP;
    private String physicalMachineVirtualIP;
    private String physicalMachineVirtualNetmask;
    private String physicalMachineMAC;
    private String physicalMachineVirtualMAC;
    private int physicalMachineCores;
    private long physicalMachineDisk;
    private int physicalMachineRAMMemory;
    private String physicalMachineArchitecture;
    private String physicalMachineHypervisorPath;
    private int physicalMachineState;
    private int physicalMachineRow;
    private int physicalMachineColumn;
    @Transient
    private transient boolean physicalMachineEnable;
    @ManyToOne(optional = true)
    private Laboratory laboratory;
    @OneToMany(mappedBy = "physicalMachine", fetch = FetchType.LAZY)
    private List<VirtualMachine> virtualMachines;
    @OneToOne
    private OperatingSystem operatingSystem;
    private double expectedFailures;
    private double averageAvailability;
    @Transient
    private transient List<ExecutionSlot> executionsSlots;
    @Transient
    private transient boolean unaviable;
    private int physicalmachinevirtualmachineson;
    private int maxvirtualmachineson;
    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getPhysicalMachineName() != null ? getPhysicalMachineName().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.losandes.persistence.entity.PhysicalMachine[id=" + getPhysicalMachineName() + "]";
    }

    /**
     * @return the physicalMachineName
     */
    public String getPhysicalMachineName() {
        return physicalMachineName;
    }

    /**
     * @param physicalMachineName the physicalMachineName to set
     */
    public void setPhysicalMachineName(String physicalMachineName) {
        this.physicalMachineName = physicalMachineName;
    }

    /**
     * @return the physicalMachineState
     */
    public int getPhysicalMachineState() {
        return physicalMachineState;
    }

    /**
     * @param physicalMachineState the physicalMachineState to set
     */
    public void setPhysicalMachineState(int physicalMachineState) {
        this.physicalMachineState = physicalMachineState;
    }

    /**
     * @return the physicalMachineIP
     */
    public String getPhysicalMachineIP() {
        return physicalMachineIP;
    }

    /**
     * @param physicalMachineIP the physicalMachineIP to set
     */
    public void setPhysicalMachineIP(String physicalMachineIP) {
        this.physicalMachineIP = physicalMachineIP;
    }

    /**
     * @return the physicalMachineMAC
     */
    public String getPhysicalMachineMAC() {
        return physicalMachineMAC;
    }

    /**
     * @param physicalMachineMAC the physicalMachineMAC to set
     */
    public void setPhysicalMachineMAC(String physicalMachineMAC) {
        this.physicalMachineMAC = physicalMachineMAC;
    }

    /**
     * @return the physicalMachineCores
     */
    public int getPhysicalMachineCores() {
        return physicalMachineCores;
    }

    /**
     * @param physicalMachineCores the physicalMachineCores to set
     */
    public void setPhysicalMachineCores(int physicalMachineCores) {
        this.physicalMachineCores = physicalMachineCores;
    }

    /**
     * @return the physicalMachineDisk
     */
    public long getPhysicalMachineDisk() {
        return physicalMachineDisk;
    }

    /**
     * @param physicalMachineDisk the physicalMachineDisk to set
     */
    public void setPhysicalMachineDisk(long physicalMachineDisk) {
        this.physicalMachineDisk = physicalMachineDisk;
    }

    /**
     * @return the physicalMachineRAMMemory
     */
    public int getPhysicalMachineRAMMemory() {
        return physicalMachineRAMMemory;
    }

    /**
     * @param physicalMachineRAMMemory the physicalMachineRAMMemory to set
     */
    public void setPhysicalMachineRAMMemory(int physicalMachineRAMMemory) {
        this.physicalMachineRAMMemory = physicalMachineRAMMemory;
    }

    /**
     * @return the physicalMachineArchitecture
     */
    public String getPhysicalMachineArchitecture() {
        return physicalMachineArchitecture;
    }

    /**
     * @param physicalMachineArchitecture the physicalMachineArchitecture to set
     */
    public void setPhysicalMachineArchitecture(String physicalMachineArchitecture) {
        this.physicalMachineArchitecture = physicalMachineArchitecture;
    }

    /**
     * @return the physicalMachineHypervisorPath
     */
    public String getPhysicalMachineHypervisorPath() {
        return physicalMachineHypervisorPath;
    }

    /**
     * @param physicalMachineHypervisorPath the physicalMachineHypervisorPath to set
     */
    public void setPhysicalMachineHypervisorPath(String physicalMachineHypervisorPath) {
        this.physicalMachineHypervisorPath = physicalMachineHypervisorPath;
    }

    /**
     * @return the physicalMachineRow
     */
    public int getPhysicalMachineRow() {
        return physicalMachineRow;
    }

    /**
     * @param physicalMachineRow the physicalMachineRow to set
     */
    public void setPhysicalMachineRow(int physicalMachineRow) {
        this.physicalMachineRow = physicalMachineRow;
    }

    /**
     * @return the physicalMachineColumn
     */
    public int getPhysicalMachineColumn() {
        return physicalMachineColumn;
    }

    /**
     * @param physicalMachineColumn the physicalMachineColumn to set
     */
    public void setPhysicalMachineColumn(int physicalMachineColumn) {
        this.physicalMachineColumn = physicalMachineColumn;
    }

    /**
     * @return the laboratory
     */
    public Laboratory getLaboratory() {
        return laboratory;
    }

    /**
     * @param laboratory the laboratory to set
     */
    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    /**
     * @return the physicalMachineEnable
     */
    public boolean isPhysicalMachineEnable() {
        return physicalMachineEnable;
    }

    /**
     * @param physicalMachineEnable the physicalMachineEnable to set
     */
    public void setPhysicalMachineEnable(boolean physicalMachineEnable) {
        this.physicalMachineEnable = physicalMachineEnable;
    }

    /**
     * @return the physicalMachineUser
     */
    public String getPhysicalMachineUser() {
        return physicalMachineUser;
    }

    /**
     * @param physicalMachineUser the physicalMachineUser to set
     */
    public void setPhysicalMachineUser(String physicalMachineUser) {
        this.physicalMachineUser = physicalMachineUser;
    }

    /**
     * @return the virtualMachines
     */
    public List<VirtualMachine> getVirtualMachines() {
        return virtualMachines;
    }

    /**
     * @param virtualMachines the virtualMachines to set
     */
    public void setVirtualMachines(List<VirtualMachine> virtualMachines) {
        this.virtualMachines = virtualMachines;
    }

    /**
     * @return the operatingSystem
     */
    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * @param operatingSystem the operatingSystem to set
     */
    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getPhysicalMachineVirtualIP() {
        return physicalMachineVirtualIP;
    }

    public void setPhysicalMachineVirtualIP(String physicalMachineVirtualIP) {
        this.physicalMachineVirtualIP = physicalMachineVirtualIP;
    }

    public String getPhysicalMachineVirtualNetmask() {
        return physicalMachineVirtualNetmask;
    }

    public void setPhysicalMachineVirtualNetmask(String physicalMachineVirtualNetmask) {
        this.physicalMachineVirtualNetmask = physicalMachineVirtualNetmask;
    }

    public String getPhysicalMachineVirtualMAC() {
        return physicalMachineVirtualMAC;
    }

    public void setPhysicalMachineVirtualMAC(String physicalMachineVirtualMAC) {
        this.physicalMachineVirtualMAC = physicalMachineVirtualMAC;
    }

    public double getAverageAvailability() {
        return averageAvailability;
    }

    public void setAverageAvailability(double averageAvailability) {
        this.averageAvailability = averageAvailability;
    }

    public double getExpectedFailures() {
        return expectedFailures;
    }

    public void setExpectedFailures(double expectedFailures) {
        this.expectedFailures = expectedFailures;
    }

    public List<ExecutionSlot> getExecutionsSlots() {
        return executionsSlots;
    }

    public void setExecutionsSlots(List<ExecutionSlot> executionsSlots) {
        this.executionsSlots = executionsSlots;
    }

    @Transient
    public boolean getUnaviable() {
        return (physicalMachineState != ON_STATE) || unaviable;
    }

    @Transient
    public void setUnaviable(boolean unaviable) {
        this.unaviable = unaviable;
    }

    public int getPhysicalmachinevirtualmachineson() {
        return physicalmachinevirtualmachineson;
    }

    public void setPhysicalmachinevirtualmachineson(int physicalmachinevirtualmachineson) {
        this.physicalmachinevirtualmachineson = physicalmachinevirtualmachineson;
    }

    public int getMaxvirtualmachineson() {
        return maxvirtualmachineson;
    }

    public void setMaxvirtualmachineson(int maxvirtualmachineson) {
        this.maxvirtualmachineson = maxvirtualmachineson;
    }
    public String getStatusImage(){
        if(physicalMachineState==ON_STATE)return "/img/green.png";
        if(physicalMachineState==ERROR_STATE)return "/img/red.png";
        if(physicalMachineState==DEPLOYING_STATE)return "/img/amber.png";
        if(physicalMachineState==OFF_STATE)return "/img/gray.png";
        return "/img/blue.png";
    }
    

}//end of PhysicalMachine

