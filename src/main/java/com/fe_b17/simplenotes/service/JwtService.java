package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.models.Session;
import com.fe_b17.simplenotes.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {


    @Value("${jwt.secret}")
    private String jwtSecret;
    private Key key;
    private final SessionService sessionService;

    public Key getKey(){
        if(this.key == null){
            this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }
        return key;
    }

    public Map<String, Object> generateTokenAndExpiration(User user, String ipAddress, String userAgent) {

        Session session = sessionService.createSession(user, ipAddress,userAgent);

        Date expirationDate = new Date(System.currentTimeMillis() +1000 *60*60*24);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId,", user.getId());
        claims.put("jti", session.getId().toString());

        String token = buildTokenWithSession(user.getEmail(), claims, expirationDate);

        Map<String,Object> result = new HashMap<>();
        result.put("token",token);
        result.put("expirationDate",expirationDate);
        return result;
    }

    public String buildTokenWithSession(String subject, Map<String, Object> claims, Date expiration){
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(getKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e){
            System.out.println("Token is invalid" + e.getMessage());
            return false;
        }
    }

    public Claims extractAllclaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserEmail(String token){
        return extractAllclaims(token).getSubject();
    }

    public UUID extractSessionId(String token) {
        String jti = extractAllclaims(token).get("jti", String.class);
        if (jti == null || jti.isBlank()) {
            throw new IllegalStateException("JWT enthält keine jti (Session-ID)");
        }
        return UUID.fromString(jti);
    }

}
