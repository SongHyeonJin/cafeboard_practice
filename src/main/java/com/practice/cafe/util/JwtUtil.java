package com.practice.cafe.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.cafe.dto.UserRole;
import com.practice.cafe.security.UserDetailsServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserDetailsServiceImpl userDetailsService;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}")
    private String secretKey;

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final long TOKEN_EXPIRATION = 60 * 60 * 1000L; // 1 hour

    private byte[] secretKeyBytes;

    @PostConstruct
    public void init() {
        secretKeyBytes = Base64.getDecoder().decode(secretKey);
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String createToken(Long id, String email, String name, UserRole role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TOKEN_EXPIRATION);

        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORIZATION_KEY, role);
        claims.put("id", id);
        claims.put("name", name);

        return BEARER_PREFIX + buildToken(email, claims, now, expiration);
    }

    private String buildToken(String subject, Map<String, Object> claims, Date issuedAt, Date expiration) {
        String header = encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = null;
        try {
            payload = encodeToString("{\"sub\":\"" + subject + "\",\"iat\":" + issuedAt.getTime() / 1000 +
                    ",\"exp\":" + expiration.getTime() / 1000 + ",\"claims\":" + new ObjectMapper().writeValueAsString(claims) + "}");
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize claims to JSON", e);
        }
        String signature = generateSignature(header + "." + payload);

        return header + "." + payload + "." + signature;
    }

    private String encodeToString(String value) {
        return Base64.getUrlEncoder().encodeToString(value.getBytes());
    }

    private String generateSignature(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, HMAC_ALGORITHM);
            sha256_HMAC.init(secretKeySpec);
            return Base64.getUrlEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to generate JWT signature", e);
        }
    }

    public boolean validateToken(String token) {
        if (token == null || !token.startsWith(BEARER_PREFIX)) {
            return false;
        }

        token = token.substring(BEARER_PREFIX.length());
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }

        String headerPayload = parts[0] + "." + parts[1];
        String signature = parts[2];

        String calculatedSignature = generateSignature(headerPayload);
        return calculatedSignature.equals(signature);
    }

    public Map<String, Object> getUserInfoFromToken(String token) {
        token = token.substring(BEARER_PREFIX.length());
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token");
        }
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> claims = objectMapper.readValue(payload, HashMap.class);
            return (Map<String, Object>) claims.get("claims");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token payload", e);
        }
    }

    public Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
