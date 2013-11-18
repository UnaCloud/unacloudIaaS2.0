/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.deploy;

/**
 *
 * @author Clouder
 */
public enum IPGenerationPolicy {

    PUBLIC_MACHINE_BASED,DHCP,PRIVATE;
    public String nameString = name().replace("_"," ").toLowerCase();
}
