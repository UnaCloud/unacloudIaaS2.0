/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.beans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Clouder
 */

@Entity
@Table (name = "nodestatelog")
@NamedQueries({
    @NamedQuery(name = "NodeStateLog.findAll", query = "SELECT n FROM Nodestatelog n"),
    @NamedQuery(name = "NodeStateLog.findByPhysicalMachineName", query = "SELECT n FROM Nodestatelog n WHERE n.physicalmachine_physicalmachinename = :physicalmachinename "),
    @NamedQuery(name = "NodeStateLog.findByClientState", query = "SELECT n FROM Nodestatelog n WHERE n.clientstate = :clientstate"),
    @NamedQuery(name = "NodeStateLog.findByServicesNotRunning", query = "SELECT n FROM Nodestatelog n WHERE n.servicesnotrunning LIKE '%:laboratoryname%'")})
  
public class NodeStateLog implements Serializable{
    @Id
    @Basic(optional = false)
    @JoinColumn(name = "PHYSICALMACHINE_PHYSICALMACHINENAME", referencedColumnName = "PHYSICALMACHINENAME")
    private String physicalMachineName;
    @Column (name="CLIENTSTATE")
    private String clientState;
    @Column (name="SERVICESNOTRUNNING")
    private String servicesNotRunning;

    public String getPhysicalMachineName() {
        return physicalMachineName;
    }

    public void setPhysicalMachineName(String physicalMachineName) {
        this.physicalMachineName = physicalMachineName;
    }

    public String getClientState() {
        return clientState;
    }

    public void setClientState(String clientState) {
        this.clientState = clientState;
    }

    public String getServicesNotRunning() {
        return servicesNotRunning;
    }

    public void setServicesNotRunning(String servicesNotRunning) {
        this.servicesNotRunning = servicesNotRunning;
    }
    
 }
