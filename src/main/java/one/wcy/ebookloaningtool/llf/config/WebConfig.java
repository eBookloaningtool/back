package one.wcy.ebookloaningtool.llf.config;

import one.wcy.ebookloaningtool.llf.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //拦截器
    @Autowired
    private LoginInterceptor logininterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录接口、注册接口、忘记密码接口和电子书管理不拦截
        //评论系统中获取书籍评论、获取评论信息不拦截
        //获取类别接口不拦截
        registry.addInterceptor(logininterceptor)
                .excludePathPatterns("/api/users/register", "/api/auth/login", "/api/auth/forget",
                        "/api/books/get", "/api/books/popular", "/api/books/search",
                        "/api/reviews/book", "/api/reviews/content", "/api/categories/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://borrowbee.wcy.one", "http://localhost:5173", "http://localhost:5175", "http://localhost:8000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
