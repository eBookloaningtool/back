package one.wcy.ebookloaningtool.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Email service using SMTP2GO API to send emails
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    
    private final RestTemplate restTemplate;
    
    @Value("${smtp2go.api.url}")
    private String apiUrl;
    
    @Value("${smtp2go.api.key}")
    private String apiKey;
    
    @Value("${smtp2go.sender.email}")
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
            // Create request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Smtp2go-Api-Key", apiKey);
            
            // Create request body
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("sender", senderEmail);
            requestMap.put("to", toList);
            
            if (ccList != null && !ccList.isEmpty()) {
                requestMap.put("cc", ccList);
            }
            
            if (bccList != null && !bccList.isEmpty()) {
                requestMap.put("bcc", bccList);
            }
            
            requestMap.put("subject", subject);
            
            if (textBody != null && !textBody.isEmpty()) {
                requestMap.put("text_body", textBody);
            }
            
            if (htmlBody != null && !htmlBody.isEmpty()) {
                requestMap.put("html_body", htmlBody);
            }
            
            // Create HTTP request
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestMap, headers);
            
            // Send request
            ResponseEntity<EmailResponse> response = restTemplate.postForEntity(apiUrl, request, EmailResponse.class);
            
            // Process response
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                EmailResponse emailResponse = response.getBody();
                EmailResponse.EmailData data = emailResponse.getData();
                
                if (data != null && data.getSucceeded() > 0) {
                    log.info("Email sent successfully, ID: {}, Succeeded: {}", data.getEmailId(), data.getSucceeded());
                    return true;
                } else if (data != null && data.getFailed() > 0 && data.getFailures() != null) {
                    data.getFailures().forEach(failure -> 
                        log.error("Email sending failed, Recipient: {}, Error code: {}, Error message: {}", 
                            failure.getRecipient(), failure.getErrorCode(), failure.getErrorMessage())
                    );
                }
            }
            
            log.error("Email sending failed, Status code: {}, Response body: {}", response.getStatusCode(), response.getBody());
            return false;
            
        } catch (Exception e) {
            log.error("Exception occurred during email sending", e);
            return false;
        }
    }
}
