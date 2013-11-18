/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.mail;

import javax.ejb.Local;

/**
 *
 * @author Clouder
 */
@Local
public interface MailSenderLocal {

    public String sendMail(String subject,String content,String ... from);

    public void setProperties(boolean enable,String smtpServer,int port,boolean auth,String user,String pass,boolean tls);
    
}
