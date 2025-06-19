package com.fe_b17.simplenotes;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

public class Main {
    public static void main(String[] args) {

          String secretKey = "Dieser-geheime-Schlüssel-muss-versteckt-werden";
          Key key = Keys.hmacShaKeyFor(secretKey.getBytes());


        String token = Jwts.builder()
                .setSubject("alice2@example.com")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(new Date (System.currentTimeMillis() +1000*60*60*24))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println(token);

    }


}
