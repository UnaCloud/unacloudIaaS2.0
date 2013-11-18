/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.persistence;

import com.losandes.persistence.entity.*;
import com.losandes.utils.VariableManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author Clouder
 */
@Stateless
public class PersistenceServicesLocal implements IPersistenceServices{

    static{
        VariableManager.init("./vars");
    }

    @javax.persistence.PersistenceContext
    private EntityManager em;

    @Override
    public boolean create(Object obj) {
        try {
            em.persist( obj);
            return true;
        } catch (NoResultException ex) {
            System.out.println("Error Code: C0002" + ex.getMessage());
            //Domain/domain1/logs
            Logger.getLogger(Object.class.getName()).log(Level.SEVERE, null, "Error Code: C0001" + ex);
            return false;
        }
    }

    @Override
    public boolean createorUpdate(Object obj) {
        try {
            if(em.contains(obj)){
                System.out.println("EM contiene: "+obj);
                em.merge(obj);
            }else{
                System.out.println("EM crear: "+obj);
                em.persist( obj);
            }
            return true;
        } catch (NoResultException ex) {
            System.out.println("Error Code: C0002" + ex.getMessage());
            return false;
        }
    }

    @Override
    public Object update(Object obj) {
        try {
            return em.merge(obj);
        } catch (NoResultException ex) {
            System.out.println("Error Code: C0003" + ex.getMessage());
            //Domain/domain1/logs
            Logger.getLogger(PersistenceServicesLocal.class.getName()).log(Level.SEVERE, null, "Error Code: C0001" + ex);
            return null;
        }
    }

    @Override
    public boolean delete(Object obj) {
        try {
            em.remove(obj);
            return true;
        } catch (NoResultException ex) {
            System.out.println("Error Code: C0004" + ex.getMessage());
            Logger.getLogger(PersistenceServicesLocal.class.getName()).log(Level.SEVERE, null, "Error Code: C0001" + ex);
            return false;
        }
    }

    @Override
    public List findAll(Class c){
        List b =em.createQuery("SELECT O FROM  "+c.getSimpleName()+" AS O").setHint(QueryHints.REFRESH,HintValues.TRUE).getResultList();
        return b;
    }

    @Override
    public Object findById(Class c, Object id) {
        HashMap findProperties = new HashMap();
        findProperties.put(QueryHints.CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
        findProperties.put(QueryHints.CACHE_STORE_MODE, CacheStoreMode.USE);
        return em.find(c, id,findProperties);
    }

    @Override
    public Object findByName(Class c,Object name) {
        try {
            if(c==Application.class)return em.createNamedQuery("Application.findByApplicationName").setHint(QueryHints.REFRESH,HintValues.TRUE).setParameter("ApplicationName", name).getSingleResult();
            else if(c==Hypervisor.class)return em.createNamedQuery("Hypervisor.findByHypervisorName").setHint(QueryHints.REFRESH,HintValues.TRUE).setParameter("hypervisorName", name).getSingleResult();
            else if(c==Laboratory.class)return em.createNamedQuery("Laboratory.findByLaboratoryName").setHint(QueryHints.REFRESH,HintValues.TRUE).setParameter("laboratoryName", name).getSingleResult();
            else if(c==LaboratoryType.class)return em.createNamedQuery("LaboratoryType.findByLaboratoryTypeName").setHint(QueryHints.REFRESH,HintValues.TRUE).setParameter("laboratoryTypeName", name).getSingleResult();
            else if(c==PhysicalMachine.class)return em.createNamedQuery("PhysicalMachine.findByPhysicalMachineName").setHint(QueryHints.REFRESH,HintValues.TRUE).setParameter("physicalMachineName", name).getSingleResult();
            else if(c==Template.class)return em.createNamedQuery("Template.findByTemplateName").setHint(QueryHints.REFRESH,HintValues.TRUE).setParameter("templateName", name).getSingleResult();
            else if(c==SystemUser.class)return em.createNamedQuery("SystemUser.findBySystemUserName").setHint(QueryHints.REFRESH,HintValues.TRUE).setParameter("systemUserName", name).getSingleResult();
            else if(c==VirtualMachineExecution.class)return em.createNamedQuery("VirtualMachineExecution.findByVirtualMachineExecutionName").setHint(QueryHints.REFRESH,HintValues.TRUE).setParameter("VirtualMachineExecutionName", name).getSingleResult();
            else if(c==VirtualMachine.class)return em.createNamedQuery("VirtualMachine.findByVirtualMachineName").setHint(QueryHints.REFRESH,HintValues.TRUE).setParameter("VirtualMachineName", name).getSingleResult();
            else if(c==VirtualMachineSecurity.class)return em.createNamedQuery("VirtualMachineSecurity.findByVirtualMachineSecurityName").setHint(QueryHints.REFRESH,HintValues.TRUE).setParameter("VirtualMachineSecurityName", name).getSingleResult();

        } catch (NoResultException ex) {
            System.out.println("Error Code: C0001" + ex.getMessage());
            //Domain/domain1/logs
            Logger.getLogger(PersistenceServicesLocal.class.getName()).log(Level.SEVERE, null, "Error Code: C0001" + ex);
            return null;
        }
        return null;
    }

    @Override
    public List executeNativeSQLList(String sentence, Class c) {
        if(c==null){
            return em.createNativeQuery(sentence).getResultList();
        }else{
            return em.createNativeQuery(sentence, c).setHint(QueryHints.REFRESH,HintValues.TRUE).getResultList();
        }
    }

    @Override
    public String executeSQLString(String sentence) {
        em.createNativeQuery(sentence).executeUpdate();
        return null;
    }

    public String getProperty(String key){
        Object c = em.find(Constant.class, key);
        if(c==null)return null;
        return ((Constant)c).getValue();
    }

    public void setProperty(String key,String value){
        Constant c = new Constant();
        c.setKeyString(key);
        c.setValue(value);
        if(getProperty(c.getKeyString())!=null)em.merge(c);
        else em.persist(c);
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        for(Map.Entry<String,String> p:properties.entrySet()){
            Constant c = new Constant();
            c.setKeyString(p.getKey());
            c.setValue(p.getValue());
            if(getProperty(p.getKey())!=null)em.merge(c);
            else em.persist(c);
        }
    }

    @Override
    public int getIntValue(String key) {
        return Integer.parseInt(getProperty("Integer."+key));
    }

    @Override
    public String getStringValue(String key) {
        return getProperty("String."+key);
    }


}
