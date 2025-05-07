/**
 * Configuration class for RestTemplate bean.
 * Provides a RestTemplate instance for making HTTP requests
 * in the notification service.
 */
package one.wcy.ebookloaningtool.notification;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    /**
     * Creates and configures a RestTemplate bean.
     * This bean is used for making HTTP requests to external services.
     *
     * @return configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}