package com.clienter.clienterbackend.config;

import com.clienter.clienterbackend.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY ="zH0s0CXGSGxXhfwxVE3gwW3YaeEUZzHk0NAveO8+NEkMchNrcKeYhNuk+WG/3s94wt5sMoP+6aXyy5gUEtGH6/wmE1fMLIELjxaqay0Arl7AQRKT2zfbkpIUajIqUWGIFMMCZin6b3rqRwJri1Roa9MIk7cLIIGlgvXzKaTvQQaRwUkMeURcUOAjCNP9Os7kHWLtPNcIVq1vk6m+ByHBbdlKPQArS5aP9hxl/8zX46thWoYw0siYjb5+cfJoOu291djji5JfmDBt5BtoYdqy5YMmSayP2HrMK1jxQ9VMhI8/FplPx06DHGqKFNlEiL6vzSBkB8qjYlN3TlC2NXZ9O62W6xQKDFwLYc7K+NitCJ4=\n";
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserEntity userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserEntity userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*69*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername((token));
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts .parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserIdFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer "
            return extractUsername(token);
        }
        return null;
    }
}