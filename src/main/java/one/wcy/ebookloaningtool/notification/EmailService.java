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
 * Service class for handling email notifications using SMTP.
 * Provides methods for sending both plain text and HTML emails to single or multiple recipients.
 * Implements error handling and logging for email operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    
    /**
     * JavaMailSender instance for sending emails via SMTP
     */
    private final JavaMailSender mailSender;
    
    /**
     * Email address used as the sender for all outgoing emails.
     * Value is injected from application properties.
     */
    @Value("${spring.mail.username}")
    private String senderEmail;
    
    /**
     * Sends a plain text email to a single recipient.
     * 
     * @param to recipient email address
     * @param subject email subject line
     * @param textBody email content in plain text format
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendTextEmail(String to, String subject, String textBody) {
        return sendEmail(Collections.singletonList(to), null, null, subject, textBody, null);
    }
    
    /**
     * Sends an HTML-formatted email to a single recipient.
     * 
     * @param to recipient email address
     * @param subject email subject line
     * @param htmlBody email content in HTML format
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendHtmlEmail(String to, String subject, String htmlBody) {
        return sendEmail(Collections.singletonList(to), null, null, subject, null, htmlBody);
    }
    
    /**
     * Sends a plain text email to multiple recipients.
     * 
     * @param toList list of recipient email addresses
     * @param subject email subject line
     * @param textBody email content in plain text format
     * @return true if the email was sent successfully to all recipients, false otherwise
     */
    public boolean sendBulkTextEmail(List<String> toList, String subject, String textBody) {
        return sendEmail(toList, null, null, subject, textBody, null);
    }
    
    /**
     * Sends an HTML-formatted email to multiple recipients.
     * 
     * @param toList list of recipient email addresses
     * @param subject email subject line
     * @param htmlBody email content in HTML format
     * @return true if the email was sent successfully to all recipients, false otherwise
     */
    public boolean sendBulkHtmlEmail(List<String> toList, String subject, String htmlBody) {
        return sendEmail(toList, null, null, subject, null, htmlBody);
    }
    
    /**
     * Core email sending method that handles both plain text and HTML emails.
     * Supports multiple recipients, CC, and BCC lists.
     * Implements error handling and logging for all email operations.
     * 
     * @param toList list of primary recipient email addresses
     * @param ccList list of CC recipient email addresses (optional)
     * @param bccList list of BCC recipient email addresses (optional)
     * @param subject email subject line
     * @param textBody email content in plain text format (optional)
     * @param htmlBody email content in HTML format (optional)
     * @return true if the email was sent successfully, false if any error occurred
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
