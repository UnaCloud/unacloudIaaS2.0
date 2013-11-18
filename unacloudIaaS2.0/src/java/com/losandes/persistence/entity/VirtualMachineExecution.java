package com.losandes.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Virtual Machine Execution entity
 */
@Entity
public class VirtualMachineExecution implements Serializable {

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
    private String virtualMachineExecutionCode;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date virtualMachineExecutionStart;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date virtualMachineExecutionStop;
    
    private int virtualMachineExecutionStatus;
    private String virtualMachineExecutionStatusMessage;

    private long virtualMachineExecutionHardDisk;
    private int virtualMachineExecutionCores;
    private int virtualMachineExecutionRAMMemory;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date virtualMachineExecutionTime;
    private String virtualMachineExecutionIP;
    @OneToOne
    private Template template;
    private SystemUser systemUser;
    @ManyToOne(optional = true)
    private VirtualMachine virtualMachine;
    @Transient
    private boolean selected;

    //atributes for progress
    private boolean showProgressBar;
    private boolean isPercentage;
    private int max;
    private int current;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getVirtualMachineExecutionCode() != null ? getVirtualMachineExecutionCode().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.losandes.persistence.entity.VirtualMachineExecution[id=" + getVirtualMachineExecutionCode() + "]";
    }

    public String getVirtualMachineExecutionCode() {
        return virtualMachineExecutionCode;
    }

    public void setVirtualMachineExecutionCode(String virtualMachineExecutionCode) {
        this.virtualMachineExecutionCode = virtualMachineExecutionCode;
    }

    

    /**
     * @return the virtualMachineExecutionStart
     */
    public Date getVirtualMachineExecutionStart() {
        return virtualMachineExecutionStart;
    }

    /**
     * @param virtualMachineExecutionStart the virtualMachineExecutionStart to set
     */
    public void setVirtualMachineExecutionStart(Date virtualMachineExecutionStart) {
        this.virtualMachineExecutionStart = virtualMachineExecutionStart;
    }

    /**
     * @return the virtualMachineExecutionStop
     */
    public Date getVirtualMachineExecutionStop() {
        return virtualMachineExecutionStop;
    }

    /**
     * @param virtualMachineExecutionStop the virtualMachineExecutionStop to set
     */
    public void setVirtualMachineExecutionStop(Date virtualMachineExecutionStop) {
        this.virtualMachineExecutionStop = virtualMachineExecutionStop;
    }

    /**
     * @return the virtualMachineExecutionStatus
     */
    public int getVirtualMachineExecutionStatus() {
        return virtualMachineExecutionStatus;
    }

    /**
     * @param virtualMachineExecutionStatus the virtualMachineExecutionStatus to set
     */
    public void setVirtualMachineExecutionStatus(int virtualMachineExecutionStatus) {
        this.virtualMachineExecutionStatus = virtualMachineExecutionStatus;
    }

    /**
     * @return the virtualMachineExecutionHardDisk
     */
    public long getVirtualMachineExecutionHardDisk() {
        return virtualMachineExecutionHardDisk;
    }

    /**
     * @param virtualMachineExecutionHardDisk the virtualMachineExecutionHardDisk to set
     */
    public void setVirtualMachineExecutionHardDisk(long virtualMachineExecutionHardDisk) {
        this.virtualMachineExecutionHardDisk = virtualMachineExecutionHardDisk;
    }

    /**
     * @return the virtualMachineExecutionCores
     */
    public int getVirtualMachineExecutionCores() {
        return virtualMachineExecutionCores;
    }

    /**
     * @param virtualMachineExecutionCores the virtualMachineExecutionCores to set
     */
    public void setVirtualMachineExecutionCores(int virtualMachineExecutionCores) {
        this.virtualMachineExecutionCores = virtualMachineExecutionCores;
    }

    /**
     * @return the virtualMachineExecutionRAMMemory
     */
    public int getVirtualMachineExecutionRAMMemory() {
        return virtualMachineExecutionRAMMemory;
    }

    /**
     * @param virtualMachineExecutionRAMMemory the virtualMachineExecutionRAMMemory to set
     */
    public void setVirtualMachineExecutionRAMMemory(int virtualMachineExecutionRAMMemory) {
        this.virtualMachineExecutionRAMMemory = virtualMachineExecutionRAMMemory;
    }

    /**
     * @return the virtualMachineExecutionIP
     */
    public String getVirtualMachineExecutionIP() {
        return virtualMachineExecutionIP;
    }

    /**
     * @param virtualMachineExecutionIP the virtualMachineExecutionIP to set
     */
    public void setVirtualMachineExecutionIP(String virtualMachineExecutionIP) {
        this.virtualMachineExecutionIP = virtualMachineExecutionIP;
    }

    /**
     * @return the virtualMachine
     */
    public VirtualMachine getVirtualMachine() {
        return virtualMachine;
    }

    /**
     * @param virtualMachine the virtualMachine to set
     */
    public void setVirtualMachine(VirtualMachine virtualMachine) {
        this.virtualMachine = virtualMachine;
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

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    

    /**
     * @return the virtualMachineExecutionTime
     */
    public Date getVirtualMachineExecutionTime() {
        return virtualMachineExecutionTime;
    }

    /**
     * @param virtualMachineExecutionTime the virtualMachineExecutionTime to set
     */
    public void setVirtualMachineExecutionTime(Date virtualMachineExecutionTime) {
        this.virtualMachineExecutionTime = virtualMachineExecutionTime;
    }

    public String getVirtualMachineExecutionStatusMessage() {
        return virtualMachineExecutionStatusMessage;
    }

    public void setVirtualMachineExecutionStatusMessage(String virtualMachineExecutionStatusMessage) {
        this.virtualMachineExecutionStatusMessage = virtualMachineExecutionStatusMessage;
    }

    public boolean getUserOperationsEnable(){
        return virtualMachineExecutionStatus==ON_STATE;
    }

    public boolean getExecutionFailed(){
        return virtualMachineExecutionStatus==ERROR_STATE;
    }

    @Transient
    public String getStatusImage(){
        if(virtualMachineExecutionStatus==ON_STATE)return "/img/green.png";
        if(virtualMachineExecutionStatus==ERROR_STATE)return "/img/red.png";
        if(virtualMachineExecutionStatus==DEPLOYING_STATE)return "/img/amber.png";
        return "/img/blue.png";
    }

    @Transient
    public String getRemainingTime(){
        long t = (getVirtualMachineExecutionTime().getTime()-System.currentTimeMillis())/1000l;
        String s = ""+t%60;
        String m = ""+(t/=60)%60;
        String h = ""+(t/=60);
        if(s.length()==1)s="0"+s;
        if(m.length()==1)m="0"+m;
        return h+"h:"+m+"m:"+s+"s";
    }

    public int getCurrent() {
        return current;
    }

    @Transient
    public boolean isStarted() {
        return virtualMachineExecutionStatus==1;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isIsPercentage() {
        return isPercentage;
    }

    public void setIsPercentage(boolean isPercentage) {
        this.isPercentage = isPercentage;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isShowProgressBar() {
        return showProgressBar;
    }

    public void setShowProgressBar(boolean showProgressBar) {
        this.showProgressBar = showProgressBar;
    }

    @Transient
    public boolean isSelected() {
        return selected;
    }

    @Transient
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    
}// end of VirtualMachineExecution

