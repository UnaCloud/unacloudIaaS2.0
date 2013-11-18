/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.communication.messages.configuration;

/**
 *
 * @author Clouder
 */
public class ExecuteCommandRequest {
    String execName;
    String[] vars;
    public ExecuteCommandRequest() {
    }
    public ExecuteCommandRequest(String execName, String[] vars) {
        this.execName = execName;
        this.vars = vars;
    }

    public String getExecName() {
        return execName;
    }

    public String[] getVars() {
        return vars;
    }
    
}
