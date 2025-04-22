package one.wcy.ebookloaningtool.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.List;

/**
 * Email service using SMTP to send emails
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String senderEmail;
    
    /**
     * Send plain text email
     * 
     * @param to recipient address
     * @param subject email subject
     * @param textBody email content (plain text)
     * @return whether sending was successful
     */
    public boolean sendTextEmail(String to, String subject, String textBody) {
        return sendEmail(Collections.singletonList(to), null, null, subject, textBody, null);
    }
    
    /**
     * Send HTML email
     * 
     * @param to recipient address
     * @param subject email subject
     * @param htmlBody email content (HTML format)
     * @return whether sending was successful
     */
    public boolean sendHtmlEmail(String to, String subject, String htmlBody) {
        return sendEmail(Collections.singletonList(to), null, null, subject, null, htmlBody);
    }
    
    /**
     * Send bulk plain text emails
     * 
     * @param toList recipient list
     * @param subject email subject
     * @param textBody email content (plain text)
     * @return whether sending was successful
     */
    public boolean sendBulkTextEmail(List<String> toList, String subject, String textBody) {
        return sendEmail(toList, null, null, subject, textBody, null);
    }
    
    /**
     * Send bulk HTML emails
     * 
     * @param toList recipient list
     * @param subject email subject
     * @param htmlBody email content (HTML format)
     * @return whether sending was successful
     */
    public boolean sendBulkHtmlEmail(List<String> toList, String subject, String htmlBody) {
        return sendEmail(toList, null, null, subject, null, htmlBody);
    }
    
    /**
     * General method for sending emails
     * 
     * @param toList recipient list
     * @param ccList cc list
     * @param bccList bcc list
     * @param subject email subject
     * @param textBody email content (plain text)
     * @param htmlBody email content (HTML format)
     * @return whether sending was successful
     */
    public boolean sendEmail(List<String> toList, List<String> ccList, List<String> bccList, 
                          String subject, String textBody, String htmlBody) {
        try {
            log.info("Attempting to send email to: {}, Subject: {}", toList, subject);
            
            if (htmlBody != null && !htmlBody.isEmpty()) {
                // Send HTML email
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                
                helper.setFrom(senderEmail);
                helper.setTo(toList.toArray(new String[0]));
                if (ccList != null && !ccList.isEmpty()) {
                    helper.setCc(ccList.toArray(new String[0]));
                }
                if (bccList != null && !bccList.isEmpty()) {
                    helper.setBcc(bccList.toArray(new String[0]));
                }
                helper.setSubject(subject);
                helper.setText(htmlBody, true);
                
                mailSender.send(message);
            } else {
                // Send plain text email
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(senderEmail);
                message.setTo(toList.toArray(new String[0]));
                if (ccList != null && !ccList.isEmpty()) {
                    message.setCc(ccList.toArray(new String[0]));
                }
                if (bccList != null && !bccList.isEmpty()) {
                    message.setBcc(bccList.toArray(new String[0]));
                }
                message.setSubject(subject);
                message.setText(textBody != null && !textBody.isEmpty() ? textBody : " ");
                
                mailSender.send(message);
            }
            
            log.info("Email sent successfully");
            return true;
            
        } catch (MessagingException e) {
            log.error("MessagingException occurred during email sending", e);
            return false;
        } catch (org.springframework.mail.MailException e) {
            log.error("MailException occurred during email sending", e);
            return false;
        } catch (RuntimeException e) {
            log.error("Unexpected runtime exception occurred during email sending", e);
            return false;
        }
    }
}
