package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {


    @Value("${jwt.secret}")
    private String jwtSecret;
    private Key key;

    public Key getKey(){
        if(this.key == null){
            this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }
        return key;
    }



    public Map<String, Object> generateTokenAndExpiration(User user) {
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        String token = buildToken(user, expiration);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("expiresAt", expiration.getTime());
        return result;
    }

    public String buildToken(User user, Date expiration){
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(getKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean tokenMatchesUser(String token, User user){
        try {
            String email = extractUserEmail(token);
            return email.equals(user.getEmail());
        }catch (Exception e){
            return false;
        }
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

    public UUID extractUserId(String token){
        return UUID.fromString(extractAllclaims(token).get("userId",String.class));
    }

}
