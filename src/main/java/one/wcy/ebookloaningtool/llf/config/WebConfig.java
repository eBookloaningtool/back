/**
 * Web configuration class for the application.
 * Configures CORS settings and request interceptors for the web application.
 */
package one.wcy.ebookloaningtool.llf.config;

import one.wcy.ebookloaningtool.llf.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Interceptor for handling user authentication
     */
    @Autowired
    private LoginInterceptor logininterceptor;

    /**
     * Configures request interceptors for the application.
     * Excludes certain paths from authentication requirements:
     * - User registration and authentication endpoints
     * - Book retrieval and search endpoints
     * - Review system endpoints
     * - Category management endpoints
     * @param registry The interceptor registry to configure
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logininterceptor)
                .excludePathPatterns("/api/users/register", "/api/auth/login", "/api/auth/forget",
                        "/api/books/get", "/api/books/popular", "/api/books/search",
                        "/api/reviews/book", "/api/reviews/content", "/api/categories/**");
    }

    /**
     * Configures CORS (Cross-Origin Resource Sharing) settings for the application.
     * Allows cross-origin requests with specific methods, headers, and credentials.
     * @param registry The CORS registry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
