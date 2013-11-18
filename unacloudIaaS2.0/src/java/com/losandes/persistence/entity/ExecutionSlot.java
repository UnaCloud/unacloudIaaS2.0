/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.persistence.entity;
import java.io.Serializable;
import static com.losandes.utils.Constants.*;
/**
 *
 * @author Clouder
 */
public class ExecutionSlot implements Serializable{

    public int state;

    public String message;

    public ExecutionSlot() {
    }

    public ExecutionSlot(int state, String toolTipText) {
        this.state = state;
        this.message = toolTipText;
    }

    

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusImage(){
        if(state==ON_STATE)return "/img/green.png";
        if(state==ERROR_STATE)return "/img/red.png";
        if(state==DEPLOYING_STATE)return "/img/amber.png";
        if(state==OFF_STATE)return "/img/gray.png";
        return "/img/blue.png";
    }

    


}
