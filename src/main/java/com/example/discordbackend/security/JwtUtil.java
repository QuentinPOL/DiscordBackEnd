package com.example.discordbackend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.security.Key;


public class JwtUtil {

    private static final String SECRET_KEY = "0572b69695bc312a3fb1d94d78a48932171f7afb28a5d06b667804e8605888f2";

    // Génère un token JWT valide pendant 3 heures
    public static String generateToken(String email) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3)) // Expire en 3h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Vérifie un token JWT et retourne l'email de l'utilisateur
    public static String validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
