package br.com.treinaweb.twjobs.core.services.jwt;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.treinaweb.twjobs.config.JwtConfigProperties;
import br.com.treinaweb.twjobs.core.exceptions.JwtServiceException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JjwtJwtService implements JwtService {

    private final JwtConfigProperties configProperties;

    @Override
    public String generateAccessToken(String sub) {
        return generateToken(
            sub, 
            configProperties.getAccessSecret(), 
            configProperties.getAccessExpiresIn()
        );
    }

    @Override
    public String getSubFromAccessToken(String token) {
        return getSubFromToken(token, configProperties.getAccessSecret());
    }

    @Override
    public String generateRefreshToken(String sub) {
        return generateToken(
            sub, 
            configProperties.getRefreshSecret(), 
            configProperties.getRefreshExpiresIn()
        );
    }

    @Override
    public String getSubFromRefreshToken(String token) {
        return getSubFromToken(token, configProperties.getRefreshSecret());
    }

    private String generateToken(String sub, String secret, Long expiresIn) {
        var now = Instant.now();
        var expiration = now.plusSeconds(expiresIn);
        var key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
            .subject(sub)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .signWith(key)
            .compact();
    }

    private String getSubFromToken(String token, String secret) {
        var key = Keys.hmacShaKeyFor(secret.getBytes());
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        } catch (JwtException e) {
            throw new JwtServiceException(e.getLocalizedMessage());
        }
    }
    
}
