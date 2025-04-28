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

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgetPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoderService passwordEncoderService;
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * 处理忘记密码请求
     * 
     * @param email 用户邮箱
     * @return 是否成功发送重置邮件
     */
    public boolean handleForgetPassword(String email) {
        // 验证邮箱是否存在
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("Password reset requested for non-existent email: {}", email);
            // 为了安全考虑，我们依然返回true，不告诉攻击者该邮箱是否存在
            return true;
        }

        // 生成随机密码
        String newPassword = generateRandomPassword();
        String encodedPassword = passwordEncoderService.encodePassword(newPassword);
        
        // 更新用户密码
        user.setEncodedPassword(encodedPassword);
        userRepository.save(user);

        // 发送包含新密码的邮件
        String subject = "eBook borrow system - Reset password";
        String body = buildPasswordResetEmailBody(user.getName(), newPassword);
        
        boolean emailSent = emailService.sendHtmlEmail(email, subject, body);
        if (!emailSent) {
            log.error("Failed to send password reset email to: {}", email);
            return false;
        }
        
        log.info("Password reset email sent to: {}", email);
        return true;
    }

    /**
     * 生成随机密码
     */
    private String generateRandomPassword() {
        byte[] randomBytes = new byte[12];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * 构建密码重置邮件正文
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