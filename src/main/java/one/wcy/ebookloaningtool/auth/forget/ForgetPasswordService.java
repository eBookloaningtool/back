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
        String subject = "电子书借阅系统 - 密码重置";
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
                "<h2>电子书借阅系统密码重置</h2>" +
                "<p>尊敬的 " + userName + "：</p>" +
                "<p>您的账户密码已被重置。您的新密码是：</p>" +
                "<p style='font-weight: bold; font-size: 16px;'>" + newPassword + "</p>" +
                "<p>请使用此临时密码登录，并尽快在个人设置中修改为您自己的密码。</p>" +
                "<p>如果您没有请求重置密码，请立即联系管理员。</p>" +
                "<p>此致</p>" +
                "<p>电子书借阅系统团队</p>" +
                "</body></html>";
    }
} 