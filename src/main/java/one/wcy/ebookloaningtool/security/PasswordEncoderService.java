/**
 * Service for handling password encoding and verification.
 * Uses BCrypt algorithm for secure password hashing and comparison.
 */
package one.wcy.ebookloaningtool.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderService {
    
    /**
     * BCrypt password encoder instance for secure password handling
     */
    private final BCryptPasswordEncoder passwordEncoder;
    
    /**
     * Initializes the password encoder service with a new BCrypt encoder instance
     */
    public PasswordEncoderService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    /**
     * Encodes a raw password using BCrypt algorithm.
     *
     * @param rawPassword The plain text password to encode
     * @return The encoded password string
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    /**
     * Verifies if a raw password matches an encoded password.
     *
     * @param rawPassword The plain text password to verify
     * @param encodedPassword The encoded password to compare against
     * @return true if the passwords match, false otherwise
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
} 