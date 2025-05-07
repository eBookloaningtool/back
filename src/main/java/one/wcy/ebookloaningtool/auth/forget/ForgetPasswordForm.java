/**
 * Data transfer object (DTO) for password reset requests.
 * Contains the email address of the user requesting password reset.
 */
package one.wcy.ebookloaningtool.auth.forget;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ForgetPasswordForm {
    /**
     * Email address of the user requesting password reset
     */
    private String email;
} 