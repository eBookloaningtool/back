package one.wcy.ebookloaningtool.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SMTP2GO API Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponse {
    
    private String requestId;
    private EmailData data;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailData {
        private int succeeded;
        private int failed;
        private List<FailureInfo> failures;
        private String emailId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FailureInfo {
        private String recipient;
        private String errorCode;
        private String errorMessage;
    }
} 