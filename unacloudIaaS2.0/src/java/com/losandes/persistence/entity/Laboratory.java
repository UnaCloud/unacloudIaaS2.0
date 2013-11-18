package com.losandes.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for implementing the Laboratory entity
 */
@Entity
@NamedQueries({@NamedQuery(name = "Laboratory.findByLaboratoryName", query = "SELECT ln FROM Laboratory ln WHERE ln.laboratoryName = :laboratoryName")})
public class Laboratory implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int laboratoryCode;
    private String laboratoryName;
    private int laboratoryRows;
    private int laboratoryColumns;
    @OneToOne
    private LaboratoryType laboratoryType;
    @OneToMany(mappedBy = "laboratory", fetch = FetchType.LAZY)
    private List<PhysicalMachine> physicalMachine;
    @Transient
    private boolean selected;

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
        hash += (getLaboratoryCode() != null ? getLaboratoryCode().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.losandes.persistence.entity.Laboratory[id=" + getLaboratoryCode() + "]";
    }

    /**
     * @return the laboratoryCode
     */
    @Id
    public Integer getLaboratoryCode() {
        return laboratoryCode;
    }

    /**
     * @param laboratoryCode the laboratoryCode to set
     */
    public void setLaboratoryCode(Integer LaboratoryCode) {
        this.laboratoryCode = LaboratoryCode;
    }

    /**
     * @return the laboratoryName
     */
    public String getLaboratoryName() {
        return laboratoryName;
    }

    /**
     * @param laboratoryName the laboratoryName to set
     */
    public void setLaboratoryName(String LaboratoryName) {
        this.laboratoryName = LaboratoryName;
    }

    /**
     * @return the laboratoryRows
     */
    public int getLaboratoryRows() {
        return laboratoryRows;
    }

    /**
     * @param laboratoryRows the laboratoryRows to set
     */
    public void setLaboratoryRows(int LaboratoryRows) {
        this.laboratoryRows = LaboratoryRows;
    }

    /**
     * @return the laboratoryColumns
     */
    public int getLaboratoryColumns() {
        return laboratoryColumns;
    }

    /**
     * @param laboratoryColumns the laboratoryColumns to set
     */
    public void setLaboratoryColumns(int LaboratoryColumns) {
        this.laboratoryColumns = LaboratoryColumns;
    }

    /**
     * @return the laboratoryType
     */
    public LaboratoryType getLaboratoryType() {
        return laboratoryType;
    }

    /**
     * @param laboratoryType the laboratoryType to set
     */
    public void setLaboratoryType(LaboratoryType laboratoryType) {
        this.laboratoryType = laboratoryType;
    }

    /**
     * @return the physicalMachine
     */
    public List<PhysicalMachine> getPhysicalMachine() {
        return physicalMachine;
    }

    /**
     * @param physicalMachine the physicalMachine to set
     */
    public void setPhysicalMachine(List<PhysicalMachine> physicalMachine) {
        this.physicalMachine = physicalMachine;
    }

    @Transient
    public int getSize() {
        return physicalMachine.size();
    }
    @Transient
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }



}//end of  Laboratory

