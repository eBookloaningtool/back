package one.wcy.ebookloaningtool.llf.config;

import one.wcy.ebookloaningtool.llf.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //拦截器
    @Autowired
    private LoginInterceptor logininterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录接口和注册接口不拦截,电子书管理不拦截
        registry.addInterceptor(logininterceptor).excludePathPatterns("/api/users/register", "/api/auth/login","/api/books/**");
    }
}
