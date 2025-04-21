package one.wcy.ebookloaningtool.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import one.wcy.ebookloaningtool.users.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtTokenService {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Token ID -> TokenInfo
    private final Map<String, TokenInfo> activeTokens = new ConcurrentHashMap<>();

    @Value("${jwt.expiration:3600}")
    private long expirationTime; // 3600s = 1h

    public String generateToken(User user) {
        // Create Token ID
        String tokenId = UUID.randomUUID().toString();

        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getUuid());
        claims.put("email", user.getEmail());
        claims.put("jti", tokenId); // JWT ID

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + expirationTime * 1000);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
        // Put token to whitelist
        activeTokens.put(tokenId, new TokenInfo(token, user.getUuid(), expiration));

        return token;
    }

//    public void invalidateToken(String token) {    //使令牌失效
//        try {
//            Claims claims = extractClaims(token);
//            String tokenId = claims.get("jti", String.class);
//            if (tokenId != null) {
//                activeTokens.remove(tokenId);
//            }
//        } catch (Exception e) {
//            // Failed token
//        }
//    }
    public void invalidateToken(Claims claims) {    //使令牌失效
        try {
            String tokenId = claims.get("jti", String.class);
            if (tokenId != null) {
                activeTokens.remove(tokenId);
            }
        } catch (Exception e) {
            // Failed token
        }
    }

    public void invalidateAllUserTokens(String userUuid) {    //使用户的所有令牌失效
        activeTokens.entrySet().removeIf(entry -> entry.getValue().userUuid().equals(userUuid));
    }

    public boolean validateToken(String token) {   //检查令牌是否有效
        try {
            Claims claims = extractClaims(token);
            if (claims.getExpiration().before(new Date())) {
                return false;
            }
            String tokenId = claims.get("jti", String.class);
            return tokenId != null && activeTokens.containsKey(tokenId);
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {   //从令牌中提取Claims
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void cleanupExpiredTokens() {   //清理过期的令牌
        Date now = new Date();
        activeTokens.entrySet().removeIf(entry -> entry.getValue().expiration().before(now));
    }

    private record TokenInfo(String token, String userUuid, Date expiration) {
    }   //令牌信息

    public Claims extractAllClaims(String token) {   //从令牌中提取所有Claims
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
} 