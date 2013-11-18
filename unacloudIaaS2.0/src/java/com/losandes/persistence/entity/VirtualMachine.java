package com.losandes.persistence.entity;

import com.losandes.deploy.IPGenerationPolicy;
import com.losandes.deploy.MACGenerationPolicy;
import com.losandes.utils.StringOperations;
import com.losandes.utils.VirtualMachineCPUStates;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Virtual Machine entity
 */
@Entity
@NamedQueries({@NamedQuery(name = "VirtualMachine.findByVirtualMachineName", query = "SELECT vmn FROM VirtualMachine vmn WHERE vmn.virtualMachineName = :virtualMachineName")})
public class VirtualMachine implements Serializable {

    private static long serialVersionUID = 1L;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int virtualMachineCode;
    private String virtualMachineName;
    private String virtualMachineIP;
    private String virtualMachineMAC;
    private int virtualMachineCores;
    private long virtualMachineHardDisk;
    private int virtualMachineRAMMemory;
    @Column(length=500)
    private String virtualMachinePath;
    private int virtualMachineState;
    @Transient
    private String vmState;
    @Transient
    private boolean deployed=true;
    @OneToOne
    private VirtualMachineSecurity virtualMachineSecurity;
    @OneToOne
    private Hypervisor hypervisor;
    @OneToOne
    private Template template;
    @ManyToOne(optional = true)
    private PhysicalMachine physicalMachine;
    //hacer get y set
    @OneToMany(cascade=CascadeType.REMOVE,mappedBy = "virtualMachine", fetch = FetchType.LAZY)
    private List<VirtualMachineExecution> virtualMachineExecutions;
    private boolean locallyStored;
    private boolean configured;
    private boolean persistent;
    private IPGenerationPolicy ipPolicy;
    private MACGenerationPolicy macPolicy;
    private boolean autoProtect;
    private String snapshotId;
    private VirtualMachineCPUStates cpuState;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getVirtualMachineCode() != null ? getVirtualMachineCode().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.losandes.persistence.entity.VirtualMachine[id=" + getVirtualMachineCode() +" "+getVirtualMachineName()+ "]";
    }

    /**
     * @return the virtualMachineCode
     */
    public Integer getVirtualMachineCode() {
        return virtualMachineCode;
    }

    /**
     * @param virtualMachineCode the virtualMachineCode to set
     */
    public void setVirtualMachineCode(Integer virtualMachineCode) {
        this.setVirtualMachineCode((int) virtualMachineCode);
    }

    /**
     * @return the virtualMachineName
     */
    public String getVirtualMachineName() {
        return virtualMachineName;
    }

    /**
     * @param virtualMachineName the virtualMachineName to set
     */
    public void setVirtualMachineName(String virtualMachineName) {
        this.virtualMachineName = virtualMachineName;
    }

    /**
     * @return the virtualMachineIP
     */
    public String getVirtualMachineIP() {
        return virtualMachineIP;
    }

    /**
     * @param virtualMachineIP the virtualMachineIP to set
     */
    public void setVirtualMachineIP(String virtualMachineIP) {
        this.virtualMachineIP = virtualMachineIP;
    }

    /**
     * @return the virtualMachineMAC
     */
    public String getVirtualMachineMAC() {
        return virtualMachineMAC;
    }

    /**
     * @param virtualMachineMAC the virtualMachineMAC to set
     */
    public void setVirtualMachineMAC(String virtualMachineMAC) {
        this.virtualMachineMAC = virtualMachineMAC;
    }

    /**
     * @return the physicalMachine
     */
    public PhysicalMachine getPhysicalMachine() {
        return physicalMachine;
    }

    /**
     * @param physicalMachine the physicalMachine to set
     */
    public void setPhysicalMachine(PhysicalMachine physicalMachine) {
        this.physicalMachine = physicalMachine;
    }

    /**
     * @param virtualMachineCode the virtualMachineCode to set
     */
    public void setVirtualMachineCode(int virtualMachineCode) {
        this.virtualMachineCode = virtualMachineCode;
    }

    /**
     * @return the virtualMachineCores
     */
    public int getVirtualMachineCores() {
        return virtualMachineCores;
    }

    /**
     * @param virtualMachineCores the virtualMachineCores to set
     */
    public void setVirtualMachineCores(int virtualMachineCores) {
        this.virtualMachineCores = virtualMachineCores;
    }

    /**
     * @return the virtualMachineRAMMemory
     */
    public int getVirtualMachineRAMMemory() {
        return virtualMachineRAMMemory;
    }

    /**
     * @param virtualMachineRAMMemory the virtualMachineRAMMemory to set
     */
    public void setVirtualMachineRAMMemory(int virtualMachineRAMMemory) {
        this.virtualMachineRAMMemory = virtualMachineRAMMemory;
    }

    /**
     * @return the virtualMachineHardDisk
     */
    public long getVirtualMachineHardDisk() {
        return virtualMachineHardDisk;
    }

    /**
     * @param virtualMachineHardDisk the virtualMachineHardDisk to set
     */
    public void setVirtualMachineHardDisk(long virtualMachineHardDisk) {
        this.virtualMachineHardDisk = virtualMachineHardDisk;
    }

    /**
     * @return the virtualMachineState
     */
    public int getVirtualMachineState() {
        return virtualMachineState;
    }

    /**
     * @param virtualMachineState the virtualMachineState to set
     */
    public void setVirtualMachineState(int virtualMachineState) {
        this.virtualMachineState = virtualMachineState;
    }

    /**
     * @return the virtualMachineExecutions
     */
    public List<VirtualMachineExecution> getVirtualMachineExecutions() {
        return virtualMachineExecutions;
    }

    /**
     * @param virtualMachineExecutions the virtualMachineExecutions to set
     */
    public void setVirtualMachineExecutions(List<VirtualMachineExecution> virtualMachineExecutions) {
        this.virtualMachineExecutions = virtualMachineExecutions;
    }

    /**
     * @return the virtualMachinePath
     */
    public String getVirtualMachinePath() {
        return virtualMachinePath;
    }

    /**
     * @param virtualMachinePath the virtualMachinePath to set
     */
    public void setVirtualMachinePath(String virtualMachinePath) {
        this.virtualMachinePath = virtualMachinePath;
    }

    /**
     * @return the hypervisor
     */
    public Hypervisor getHypervisor() {
        return hypervisor;
    }

    /**
     * @param hypervisor the hypervisor to set
     */
    public void setHypervisor(Hypervisor hypervisor) {
        this.hypervisor = hypervisor;
    }

    /**
     * @return the template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(Template template) {
        this.template = template;
    }

    /**
     * @return the virtualMachineSecurity
     */
    public VirtualMachineSecurity getVirtualMachineSecurity() {
        return virtualMachineSecurity;
    }

    /**
     * @param virtualMachineSecurity the virtualMachineSecurity to set
     */
    public void setVirtualMachineSecurity(VirtualMachineSecurity virtualMachineSecurity) {
        this.virtualMachineSecurity = virtualMachineSecurity;
    }

    /**
     * @return the vmState
     */
    public String getVmState() {
        vmState = StringOperations.getStateName(getVirtualMachineState());
        return vmState;
    }

    public boolean isLocallyStored() {
        return locallyStored;
    }

    public void setLocallyStored(boolean localyStored) {
        this.locallyStored = localyStored;
    }

    public IPGenerationPolicy getIpPolicy() {
        return ipPolicy;
    }

    public void setIpPolicy(IPGenerationPolicy ipPolicy) {
        this.ipPolicy = ipPolicy;
    }

    public MACGenerationPolicy getMacPolicy() {
        return macPolicy;
    }

    public void setMacPolicy(MACGenerationPolicy macPolicy) {
        this.macPolicy = macPolicy;
    }

    public boolean isDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public boolean isAutoProtect() {
        return autoProtect;
    }

    public void setAutoProtect(boolean autoProtect) {
        this.autoProtect = autoProtect;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public VirtualMachineCPUStates getCpuState() {
        return cpuState;
    }

    public void setCpuState(VirtualMachineCPUStates cpuState) {
        this.cpuState = cpuState;
    }

}// end of VirtualMachine

