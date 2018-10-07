/*
 * class: MailService
 */

package by.epam.admission.logic;

import by.epam.admission.util.EmailSessionCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ResourceBundle;

/**
 * @author Burishinets Maxim
 * @version 1.0 10 Sep 2018
 */
public class MailService extends Thread {

    private static final Logger LOG = LogManager.getLogger(MailService.class);

    private MimeMessage message;
    private String sendToEmail;
    private String mailSubject;
    private String mailText;
    private ResourceBundle resourceBundle;

    public MailService(String sendToEmail, String mailSubject, String mailText,
                       ResourceBundle resourceBundle) {
        this.sendToEmail = sendToEmail;
        this.mailSubject = mailSubject;
        this.mailText = mailText;
        this.resourceBundle = resourceBundle;
    }

    private void init() {
        Session mailSession =
                new EmailSessionCreator(resourceBundle).createSession();
        mailSession.setDebug(true);

        message = new MimeMessage(mailSession);
        try {
            message.setSubject(mailSubject);
            message.setContent(mailText, "text/html");
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(sendToEmail));
        } catch (AddressException e) {
            LOG.error("Invalid e-mail address", e);
        } catch (MessagingException e) {
            LOG.error("Mail generation error", e);
        }
    }

    public void run() {
        init();
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            LOG.error("Mail transfer error", e);
        }
    }
}
