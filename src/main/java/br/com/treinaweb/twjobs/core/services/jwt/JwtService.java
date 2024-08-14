package br.com.treinaweb.twjobs.core.services.jwt;

public interface JwtService {

    String generateAccessToken(String sub);
    String getSubFromAccessToken(String token);
    String generateRefreshToken(String sub);
    String getSubFromRefreshToken(String token);
    
}
