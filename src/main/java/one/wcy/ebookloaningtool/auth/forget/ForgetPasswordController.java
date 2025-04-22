package one.wcy.ebookloaningtool.auth.forget;

import lombok.RequiredArgsConstructor;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ForgetPasswordController {

    private final ForgetPasswordService forgetPasswordService;

    /**
     * 处理忘记密码请求
     * @param forgetPasswordForm 包含用户邮箱的表单
     * @return 响应结果
     */
    @PostMapping("/forget")
    public Response forgetPassword(@RequestBody ForgetPasswordForm forgetPasswordForm) {
        boolean successful = forgetPasswordService.handleForgetPassword(forgetPasswordForm.getEmail());
        
        // 无论是否成功，都返回success状态，以防止枚举攻击
        return new Response("success");
    }
} 