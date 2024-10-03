package fit.iuh.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface JwtService {
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(Map<String, Object> extractClaims, UserDetails user);
    boolean isTokenValid(String token, UserDetails userDetails);
    String extractUserName(String token);
    boolean isTokenExprired(String token);
}
