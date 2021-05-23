package com.example.demo.security;

import com.example.demo.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JWTTokenProvider {

    public static final Logger LOGGER = LoggerFactory.getLogger(JWTTokenProvider.class);

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        String userId = user.getId().toString();

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", user);
        claimsMap.put("username", user.getEmail());
        claimsMap.put("name", user.getUsername());
        claimsMap.put("surname", user.getUsername());

        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            LOGGER.error(ex.getMessage());
            return false;
        }
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody();
        Map<String, String> map = (Map<String, String>) claims.get("id");
        String id = map.get("id");

        return UUID.fromString(id);
    }
}
