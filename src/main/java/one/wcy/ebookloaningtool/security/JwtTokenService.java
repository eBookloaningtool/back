/**
 * Service for handling JWT (JSON Web Token) operations.
 * Manages token generation, validation, and invalidation for user authentication.
 * Implements token whitelist mechanism for enhanced security.
 */
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

    /**
     * Secret key used for signing and verifying JWT tokens
     */
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * Concurrent map storing active tokens and their associated information
     * Key: Token ID, Value: TokenInfo containing token details
     */
    private final Map<String, TokenInfo> activeTokens = new ConcurrentHashMap<>();

    /**
     * Token expiration time in seconds (default: 3600s = 1h)
     */
    @Value("${jwt.expiration:3600}")
    private long expirationTime;

    /**
     * Generates a new JWT token for the specified user.
     * Creates a unique token ID and stores token information in the whitelist.
     *
     * @param user User for whom the token is being generated
     * @return Generated JWT token string
     */
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

    /**
     * Invalidates a specific token using its claims.
     * Removes the token from the active tokens whitelist.
     *
     * @param claims JWT claims containing the token ID
     */
    public void invalidateToken(Claims claims) {
        try {
            String tokenId = claims.get("jti", String.class);
            if (tokenId != null) {
                activeTokens.remove(tokenId);
            }
        } catch (Exception e) {
            // Failed token
        }
    }

    /**
     * Invalidates all tokens associated with a specific user.
     * Removes all tokens from the whitelist for the given user UUID.
     *
     * @param userUuid UUID of the user whose tokens should be invalidated
     */
    public void invalidateAllUserTokens(String userUuid) {
        activeTokens.entrySet().removeIf(entry -> entry.getValue().userUuid().equals(userUuid));
    }

    /**
     * Validates a JWT token by checking its expiration and presence in the whitelist.
     *
     * @param token JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
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

    /**
     * Extracts claims from a JWT token.
     *
     * @param token JWT token to extract claims from
     * @return Claims object containing token data
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Removes expired tokens from the whitelist.
     * Called periodically to maintain the whitelist.
     */
    public void cleanupExpiredTokens() {
        Date now = new Date();
        activeTokens.entrySet().removeIf(entry -> entry.getValue().expiration().before(now));
    }

    /**
     * Record class storing token information including the token string,
     * associated user UUID, and expiration date.
     */
    private record TokenInfo(String token, String userUuid, Date expiration) {
    }

    /**
     * Extracts all claims from a JWT token.
     * Public method for accessing token claims.
     *
     * @param token JWT token to extract claims from
     * @return Claims object containing all token data
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
} 