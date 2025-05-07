/**
 * Controller class handling password reset requests.
 * Provides endpoint for initiating the password reset process.
 * Implements security measures to prevent email enumeration attacks.
 */
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

    /**
     * Service for handling password reset operations
     */
    private final ForgetPasswordService forgetPasswordService;

    /**
     * Handles password reset requests.
     * Processes the request and sends reset instructions if the email exists.
     * Always returns success to prevent email enumeration attacks.
     *
     * @param forgetPasswordForm Form containing the user's email address
     * @return Response indicating the request was received
     */
    @PostMapping("/forget")
    public Response forgetPassword(@RequestBody ForgetPasswordForm forgetPasswordForm) {
        boolean successful = forgetPasswordService.handleForgetPassword(forgetPasswordForm.getEmail());
        
        // Always return success to prevent email enumeration attacks
        return new Response("success");
    }
} 