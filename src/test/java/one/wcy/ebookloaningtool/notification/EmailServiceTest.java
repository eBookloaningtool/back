package one.wcy.ebookloaningtool.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the EmailService
 */
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmailService emailService;

    private final String API_URL = "https://api.smtp2go.com/v3/email/send";

    @BeforeEach
    public void setUp() {
        // Set private fields using ReflectionTestUtils
        ReflectionTestUtils.setField(emailService, "apiUrl", API_URL);
        String API_KEY = "api-C9064873732740C4B917A77B143A1EA5";
        ReflectionTestUtils.setField(emailService, "apiKey", API_KEY);
        String SENDER_EMAIL = "noreply@ebookloaning.wcy.one";
        ReflectionTestUtils.setField(emailService, "senderEmail", SENDER_EMAIL);
    }

    @Test
    @DisplayName("Test sending text email successfully")
    public void testSendTextEmailSuccess() {
        // Mock response
        EmailResponse.EmailData emailData = new EmailResponse.EmailData();
        emailData.setSucceeded(1);
        emailData.setFailed(0);
        emailData.setEmailId("test-email-id");

        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setRequestId("test-request-id");
        emailResponse.setData(emailData);

        when(restTemplate.postForEntity(
                eq(API_URL),
                any(HttpEntity.class),
                eq(EmailResponse.class)))
                .thenReturn(new ResponseEntity<>(emailResponse, HttpStatus.OK));

        // Test sending text email
        boolean result = emailService.sendTextEmail("wzwttol@outlook.com", "Test Subject", "Test Body");

        // Verify
        assertTrue(result, "Should return true for successful email");
        verify(restTemplate).postForEntity(eq(API_URL), any(HttpEntity.class), eq(EmailResponse.class));
    }

    @Test
    @DisplayName("Test sending HTML email successfully")
    public void testSendHtmlEmailSuccess() {
        // Mock response
        EmailResponse.EmailData emailData = new EmailResponse.EmailData();
        emailData.setSucceeded(1);
        emailData.setFailed(0);
        emailData.setEmailId("test-email-id");

        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setRequestId("test-request-id");
        emailResponse.setData(emailData);

        when(restTemplate.postForEntity(
                eq(API_URL),
                any(HttpEntity.class),
                eq(EmailResponse.class)))
                .thenReturn(new ResponseEntity<>(emailResponse, HttpStatus.OK));

        // Test sending HTML email
        boolean result = emailService.sendHtmlEmail("wzwttol@outlook.com", "Test Subject", "<p>Test HTML Body</p>");

        // Verify
        assertTrue(result, "Should return true for successful email");
        verify(restTemplate).postForEntity(eq(API_URL), any(HttpEntity.class), eq(EmailResponse.class));
    }

    @Test
    @DisplayName("Test sending bulk emails successfully")
    public void testSendBulkEmailSuccess() {
        // Mock response
        EmailResponse.EmailData emailData = new EmailResponse.EmailData();
        emailData.setSucceeded(3);
        emailData.setFailed(0);
        emailData.setEmailId("test-email-id");

        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setRequestId("test-request-id");
        emailResponse.setData(emailData);

        when(restTemplate.postForEntity(
                eq(API_URL),
                any(HttpEntity.class),
                eq(EmailResponse.class)))
                .thenReturn(new ResponseEntity<>(emailResponse, HttpStatus.OK));

        // Test sending bulk email
        List<String> recipients = Arrays.asList(
                "recipient1@example.com",
                "recipient2@example.com",
                "recipient3@example.com"
        );
        boolean result = emailService.sendBulkTextEmail(recipients, "Test Subject", "Test Body");

        // Verify
        assertTrue(result, "Should return true for successful bulk email");
        verify(restTemplate).postForEntity(eq(API_URL), any(HttpEntity.class), eq(EmailResponse.class));
    }

    @Test
    @DisplayName("Test handling API errors")
    public void testHandleApiErrors() {
        // Mock error response
        EmailResponse.EmailData emailData = new EmailResponse.EmailData();
        emailData.setSucceeded(0);
        emailData.setFailed(1);

        EmailResponse.FailureInfo failureInfo = new EmailResponse.FailureInfo();
        failureInfo.setRecipient("wzwttol@outlook.com");
        failureInfo.setErrorCode("INVALID_EMAIL");
        failureInfo.setErrorMessage("Invalid email address");

        emailData.setFailures(List.of(failureInfo));

        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setRequestId("test-error-request-id");
        emailResponse.setData(emailData);

        when(restTemplate.postForEntity(
                eq(API_URL),
                any(HttpEntity.class),
                eq(EmailResponse.class)))
                .thenReturn(new ResponseEntity<>(emailResponse, HttpStatus.OK));

        // Test sending email with API error
        boolean result = emailService.sendTextEmail("wzwttol@outlook.com", "Test Subject", "Test Body");

        // Verify
        assertFalse(result, "Should return false when API returns error");
    }

    @Test
    @DisplayName("Test handling exceptions")
    public void testHandleExceptions() {
        // Mock exception
        when(restTemplate.postForEntity(
                eq(API_URL),
                any(HttpEntity.class),
                eq(EmailResponse.class)))
                .thenThrow(new RuntimeException("Test exception"));

        // Test sending email with exception
        boolean result = emailService.sendTextEmail("wzwttol@outlook.com", "Test Subject", "Test Body");

        // Verify
        assertFalse(result, "Should return false when exception occurs");
    }
} 