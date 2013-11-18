/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.mail;

import com.losandes.persistence.IPersistenceServices;
import java.io.FileReader;
import javax.ejb.Stateless;
import java.util.Properties;
import javax.ejb.EJB;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Clouder
 */
@Stateless
public class MailSender implements MailSenderLocal {

    @EJB
    private IPersistenceServices persistence;

    @Override
    public String sendMail(String subject, String content, String... tos) {
        if (!Boolean.parseBoolean(persistence.getProperty("mail.enable"))) {
            return "Mail sending is not enable";
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", persistence.getProperty(("mail.smtpServer")));
        props.put("mail.smtp.port", persistence.getProperty(("mail.smtpPort")));
        boolean auth = Boolean.parseBoolean(persistence.getProperty(("mail.smtp.auth")));
        props.put("mail.smtp.auth", auth);
        Session session;
        if (auth) {
            session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(persistence.getProperty("username"), persistence.getProperty("passwd"));
                }
            });
        }else session=Session.getDefaultInstance(props);

        for (String to : tos) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("UnaCloudAdministrator"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setText(content);
                Transport.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        return "Mail sent";
    }

    @Override
    public void setProperties(boolean enable, String smtpServer, int port, boolean auth, String user, String pass, boolean tls) {
        persistence.setProperty("mail.Enable", enable + "");
        persistence.setProperty("mail.smtpServer", smtpServer);
        persistence.setProperty("mail.smtpPort", "" + port);
        persistence.setProperty("mail.auth", "" + auth);
        persistence.setProperty("mail.user", user);
        persistence.setProperty("mail.passwd", pass);
        persistence.setProperty("mail.tls", "" + tls);
    }
}
