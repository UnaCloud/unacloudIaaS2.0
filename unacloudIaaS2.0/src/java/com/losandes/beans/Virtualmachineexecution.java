/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Clouder
 */
@Entity
@Table(name = "virtualmachineexecution")
@NamedQueries({
    @NamedQuery(name = "Virtualmachineexecution.findAll", query = "SELECT v FROM Virtualmachineexecution v"),
    @NamedQuery(name = "Virtualmachineexecution.findByVirtualmachineexecutioncode", query = "SELECT v FROM Virtualmachineexecution v WHERE v.virtualmachineexecutioncode = :virtualmachineexecutioncode"),
    @NamedQuery(name = "Virtualmachineexecution.findByVirtualmachineexecutionstop", query = "SELECT v FROM Virtualmachineexecution v WHERE v.virtualmachineexecutionstop = :virtualmachineexecutionstop"),
    @NamedQuery(name = "Virtualmachineexecution.findByVirtualmachineexecutionstatusmessage", query = "SELECT v FROM Virtualmachineexecution v WHERE v.virtualmachineexecutionstatusmessage = :virtualmachineexecutionstatusmessage"),
    @NamedQuery(name = "Virtualmachineexecution.findByMax", query = "SELECT v FROM Virtualmachineexecution v WHERE v.max = :max"),
    @NamedQuery(name = "Virtualmachineexecution.findByVirtualmachineexecutionstatus", query = "SELECT v FROM Virtualmachineexecution v WHERE v.virtualmachineexecutionstatus = :virtualmachineexecutionstatus"),
    @NamedQuery(name = "Virtualmachineexecution.findByShowprogressbar", query = "SELECT v FROM Virtualmachineexecution v WHERE v.showprogressbar = :showprogressbar"),
    @NamedQuery(name = "Virtualmachineexecution.findByVirtualmachineexecutionrammemory", query = "SELECT v FROM Virtualmachineexecution v WHERE v.virtualmachineexecutionrammemory = :virtualmachineexecutionrammemory"),
    @NamedQuery(name = "Virtualmachineexecution.findByVirtualmachineexecutionharddisk", query = "SELECT v FROM Virtualmachineexecution v WHERE v.virtualmachineexecutionharddisk = :virtualmachineexecutionharddisk"),
    @NamedQuery(name = "Virtualmachineexecution.findByIspercentage", query = "SELECT v FROM Virtualmachineexecution v WHERE v.ispercentage = :ispercentage"),
    @NamedQuery(name = "Virtualmachineexecution.findByVirtualmachineexecutionip", query = "SELECT v FROM Virtualmachineexecution v WHERE v.virtualmachineexecutionip = :virtualmachineexecutionip"),
    @NamedQuery(name = "Virtualmachineexecution.findByVirtualmachineexecutioncores", query = "SELECT v FROM Virtualmachineexecution v WHERE v.virtualmachineexecutioncores = :virtualmachineexecutioncores"),
    @NamedQuery(name = "Virtualmachineexecution.findByVirtualmachineexecutionstart", query = "SELECT v FROM Virtualmachineexecution v WHERE v.virtualmachineexecutionstart = :virtualmachineexecutionstart"),
    @NamedQuery(name = "Virtualmachineexecution.findByCurrent", query = "SELECT v FROM Virtualmachineexecution v WHERE v.current = :current"),
    @NamedQuery(name = "Virtualmachineexecution.findByVirtualmachineexecutiontime", query = "SELECT v FROM Virtualmachineexecution v WHERE v.virtualmachineexecutiontime = :virtualmachineexecutiontime")})
public class Virtualmachineexecution implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "VIRTUALMACHINEEXECUTIONCODE")
    private String virtualmachineexecutioncode;
    @Column(name = "VIRTUALMACHINEEXECUTIONSTOP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date virtualmachineexecutionstop;
    @Column(name = "VIRTUALMACHINEEXECUTIONSTATUSMESSAGE")
    private String virtualmachineexecutionstatusmessage;
    @Column(name = "MAX")
    private Integer max;
    @Column(name = "VIRTUALMACHINEEXECUTIONSTATUS")
    private Integer virtualmachineexecutionstatus;
    @Column(name = "SHOWPROGRESSBAR")
    private Boolean showprogressbar;
    @Column(name = "VIRTUALMACHINEEXECUTIONRAMMEMORY")
    private Integer virtualmachineexecutionrammemory;
    @Column(name = "VIRTUALMACHINEEXECUTIONHARDDISK")
    private BigInteger virtualmachineexecutionharddisk;
    @Column(name = "ISPERCENTAGE")
    private Boolean ispercentage;
    @Column(name = "VIRTUALMACHINEEXECUTIONIP")
    private String virtualmachineexecutionip;
    @Column(name = "VIRTUALMACHINEEXECUTIONCORES")
    private Integer virtualmachineexecutioncores;
    @Column(name = "VIRTUALMACHINEEXECUTIONSTART")
    @Temporal(TemporalType.TIMESTAMP)
    private Date virtualmachineexecutionstart;
    @Column(name = "CURRENT")
    private Integer current;
    @Column(name = "VIRTUALMACHINEEXECUTIONTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date virtualmachineexecutiontime;
    @JoinColumn(name = "VIRTUALMACHINE_VIRTUALMACHINECODE", referencedColumnName = "VIRTUALMACHINECODE")
    @ManyToOne
    private Virtualmachine virtualmachine;
    @JoinColumn(name = "SYSTEMUSER_SYSTEMUSERNAME", referencedColumnName = "SYSTEMUSERNAME")
    @ManyToOne
    private Systemuser systemuser;
    @JoinColumn(name = "TEMPLATE_TEMPLATECODE", referencedColumnName = "TEMPLATECODE")
    @ManyToOne
    private Template template;

    public Virtualmachineexecution() {
    }

    public Virtualmachineexecution(String virtualmachineexecutioncode) {
        this.virtualmachineexecutioncode = virtualmachineexecutioncode;
    }

    public String getVirtualmachineexecutioncode() {
        return virtualmachineexecutioncode;
    }

    public void setVirtualmachineexecutioncode(String virtualmachineexecutioncode) {
        this.virtualmachineexecutioncode = virtualmachineexecutioncode;
    }

    public Date getVirtualmachineexecutionstop() {
        return virtualmachineexecutionstop;
    }

    public void setVirtualmachineexecutionstop(Date virtualmachineexecutionstop) {
        this.virtualmachineexecutionstop = virtualmachineexecutionstop;
    }

    public String getVirtualmachineexecutionstatusmessage() {
        return virtualmachineexecutionstatusmessage;
    }

    public void setVirtualmachineexecutionstatusmessage(String virtualmachineexecutionstatusmessage) {
        this.virtualmachineexecutionstatusmessage = virtualmachineexecutionstatusmessage;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getVirtualmachineexecutionstatus() {
        return virtualmachineexecutionstatus;
    }

    public void setVirtualmachineexecutionstatus(Integer virtualmachineexecutionstatus) {
        this.virtualmachineexecutionstatus = virtualmachineexecutionstatus;
    }

    public Boolean getShowprogressbar() {
        return showprogressbar;
    }

    public void setShowprogressbar(Boolean showprogressbar) {
        this.showprogressbar = showprogressbar;
    }

    public Integer getVirtualmachineexecutionrammemory() {
        return virtualmachineexecutionrammemory;
    }

    public void setVirtualmachineexecutionrammemory(Integer virtualmachineexecutionrammemory) {
        this.virtualmachineexecutionrammemory = virtualmachineexecutionrammemory;
    }

    public BigInteger getVirtualmachineexecutionharddisk() {
        return virtualmachineexecutionharddisk;
    }

    public void setVirtualmachineexecutionharddisk(BigInteger virtualmachineexecutionharddisk) {
        this.virtualmachineexecutionharddisk = virtualmachineexecutionharddisk;
    }

    public Boolean getIspercentage() {
        return ispercentage;
    }

    public void setIspercentage(Boolean ispercentage) {
        this.ispercentage = ispercentage;
    }

    public String getVirtualmachineexecutionip() {
        return virtualmachineexecutionip;
    }

    public void setVirtualmachineexecutionip(String virtualmachineexecutionip) {
        this.virtualmachineexecutionip = virtualmachineexecutionip;
    }

    public Integer getVirtualmachineexecutioncores() {
        return virtualmachineexecutioncores;
    }

    public void setVirtualmachineexecutioncores(Integer virtualmachineexecutioncores) {
        this.virtualmachineexecutioncores = virtualmachineexecutioncores;
    }

    public Date getVirtualmachineexecutionstart() {
        return virtualmachineexecutionstart;
    }

    public void setVirtualmachineexecutionstart(Date virtualmachineexecutionstart) {
        this.virtualmachineexecutionstart = virtualmachineexecutionstart;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Date getVirtualmachineexecutiontime() {
        return virtualmachineexecutiontime;
    }

    public void setVirtualmachineexecutiontime(Date virtualmachineexecutiontime) {
        this.virtualmachineexecutiontime = virtualmachineexecutiontime;
    }

    public Virtualmachine getVirtualmachine() {
        return virtualmachine;
    }

    public void setVirtualmachine(Virtualmachine virtualmachine) {
        this.virtualmachine = virtualmachine;
    }

    public Systemuser getSystemuser() {
        return systemuser;
    }

    public void setSystemuser(Systemuser systemuser) {
        this.systemuser = systemuser;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (virtualmachineexecutioncode != null ? virtualmachineexecutioncode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Virtualmachineexecution)) {
            return false;
        }
        Virtualmachineexecution other = (Virtualmachineexecution) object;
        if ((this.virtualmachineexecutioncode == null && other.virtualmachineexecutioncode != null) || (this.virtualmachineexecutioncode != null && !this.virtualmachineexecutioncode.equals(other.virtualmachineexecutioncode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.losandes.beans.Virtualmachineexecution[virtualmachineexecutioncode=" + virtualmachineexecutioncode + "]";
    }

}
