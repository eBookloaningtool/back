/**
 * Service class handling password reset operations.
 * Manages the process of resetting user passwords and sending reset instructions via email.
 * Implements secure password generation and asynchronous email notification.
 */
package one.wcy.ebookloaningtool.auth.forget;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.wcy.ebookloaningtool.notification.EmailService;
import one.wcy.ebookloaningtool.security.PasswordEncoderService;
import one.wcy.ebookloaningtool.users.User;
import one.wcy.ebookloaningtool.users.UserRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgetPasswordService {

    /**
     * Repository for user data access
     */
    private final UserRepository userRepository;

    /**
     * Service for sending email notifications
     */
    private final EmailService emailService;

    /**
     * Service for password encoding and verification
     */
    private final PasswordEncoderService passwordEncoderService;

    /**
     * Secure random number generator for password generation
     */
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Handles the password reset process for a user.
     * Generates a new random password, updates the user's password,
     * and sends an email with the new password.
     *
     * @param email Email address of the user requesting password reset
     * @return true if the process was initiated successfully
     */
    public boolean handleForgetPassword(String email) {
        // Verify if email exists
        User user = userRepository.findByEmail(email);
        AtomicReference<Boolean> flag = new AtomicReference<>(false);
        if (user == null) {
            log.warn("Password reset requested for non-existent email: {}", email);
            // Return true for security reasons, not revealing if email exists
            return true;
        }

        // Generate random password
        String newPassword = generateRandomPassword();
        String encodedPassword = passwordEncoderService.encodePassword(newPassword);
        
        // Update user password
        user.setEncodedPassword(encodedPassword);
        userRepository.save(user);
        // Send email asynchronously
        CompletableFuture.runAsync(() -> {
            try {
                // Send email with new password
                String subject = "eBook borrow system - Reset password";
                String body = buildPasswordResetEmailBody(user.getName(), newPassword);

                boolean emailSent = emailService.sendHtmlEmail(email, subject, body);
                if (!emailSent) {
                    log.error("Failed to send password reset email to: {}", email);
                    flag.set(false);
                }
                log.info("Password reset email sent to: {}", email);
                flag.set(true);
            } catch (Exception e) {
                log.error("Failed to send password reset email to: {}", email);
            }
        });
        return flag.get();
    }

    /**
     * Generates a secure random password.
     * Uses SecureRandom to generate 12 random bytes and encodes them in Base64.
     *
     * @return A secure random password string
     */
    private String generateRandomPassword() {
        byte[] randomBytes = new byte[12];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Builds the HTML email body for password reset notification.
     * Creates a formatted HTML message containing the new password and instructions.
     *
     * @param userName Name of the user
     * @param newPassword The newly generated password
     * @return HTML formatted email body
     */
    private String buildPasswordResetEmailBody(String userName, String newPassword) {
        return "<html><body>" +
                "<h2>BorrowBee</h2>" +
                "<p>Dear " + userName + "：</p>" +
                "<p>Your password already reset, your temporary password is:</p>" +
                "<p style='font-weight: bold; font-size: 16px;'>" + newPassword + "</p>" +
                "<p>Please use this temporary password to log in and change it as soon as possible.</p>" +
                "<p>If you have a question, contact us.</p>" +
                "<p>Thank you!</p>" +
                "</body></html>";
    }
} 