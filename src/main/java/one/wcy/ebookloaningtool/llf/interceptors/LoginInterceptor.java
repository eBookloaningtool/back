/**
 * Interceptor component for handling user authentication.
 * Validates JWT tokens in incoming requests and manages user session data
 * using ThreadLocal storage.
 */
package one.wcy.ebookloaningtool.llf.interceptors;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import one.wcy.ebookloaningtool.security.JwtTokenService;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * Service for handling JWT token operations
     */
    @Autowired
    private JwtTokenService jwtTokenService;

    /**
     * Intercepts incoming requests to validate authentication.
     * Checks for valid JWT token in the Authorization header and
     * stores user claims in ThreadLocal if valid.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @param handler Request handler
     * @return true if authentication is successful, false otherwise
     * @throws Exception if an error occurs during token validation
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //token validation
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ") && jwtTokenService.validateToken(authHeader.substring(7))){
            String token = authHeader.substring(7);
            //pass
            //get token data
            Claims claims = jwtTokenService.extractAllClaims(token);
            //store business data in ThreadLocal
            ThreadLocalUtil.set(claims);
            return true;
        }
        else{
            //http response status code is 401
            response.setStatus(401);
            //not pass
            return false;
        }
    }

    /**
     * Cleans up ThreadLocal data after request completion.
     * This method is called after the request has been processed,
     * ensuring that ThreadLocal storage is properly cleared.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @param handler Request handler
     * @param ex Exception that occurred during request processing, if any
     * @throws Exception if an error occurs during cleanup
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //clear data in ThreadLocal
        ThreadLocalUtil.remove();
    }
}
