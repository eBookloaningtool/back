package one.wcy.ebookloaningtool.llf.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import one.wcy.ebookloaningtool.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtTokenService jwtTokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");
        if (jwtTokenService.validateToken(token)){
            //放行
            return true;
        }
        else{
            //http响应状态码为401
            response.setStatus(401);
            //不放行
            return false;
        }
    }
}
