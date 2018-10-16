/*
 * class: EmailSessionCreator
 */

package by.epam.admission.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Class EmailSessionCreator used to create new Email session
 *
 * @author Burishinets Maxim
 * @version 1.0 20 Aug 2018
 */
public class EmailSessionCreator {

    private String smtpHost;
    private String smtpPort;
    private String userName;
    private String userPass;
    private Properties sessionProperties;

    /**
     * @param resourceBundle {@link ResourceBundle} object that contains
     *                       properties for establishing new e0mail session
     */
    public EmailSessionCreator(ResourceBundle resourceBundle) {
        smtpHost = resourceBundle.getString("mail.smtp.host");
        smtpPort = resourceBundle.getString("mail.smtp.port");
        userName = resourceBundle.getString("mail.user.name");
        userPass = resourceBundle.getString("mail.user.password");

        sessionProperties = new Properties();

        sessionProperties.setProperty("mail.transport.protocol", "smtp");
        sessionProperties.setProperty("mail.host", smtpHost);

        sessionProperties.put("mail.smtp.auth", "true");
        sessionProperties.put("mail.smtp.port", smtpPort);
        sessionProperties.put("mail.smtp.socketFactory.port", smtpPort);
        sessionProperties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        sessionProperties.put("mail.smtp.socketFactory.fallback", "false");

        sessionProperties.setProperty("mail.smtp.quitwait", "false");
    }

    public Session createEmailSession() {
        return Session.getDefaultInstance(sessionProperties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, userPass);
                    }
                });
    }

}
