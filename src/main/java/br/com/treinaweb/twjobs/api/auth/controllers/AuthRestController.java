package br.com.treinaweb.twjobs.api.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.treinaweb.twjobs.api.auth.dtos.LoginRequest;
import br.com.treinaweb.twjobs.api.auth.dtos.RefreshRequest;
import br.com.treinaweb.twjobs.api.auth.dtos.TokenResponse;
import br.com.treinaweb.twjobs.api.auth.dtos.UserRequest;
import br.com.treinaweb.twjobs.api.auth.dtos.UserResponse;
import br.com.treinaweb.twjobs.api.auth.mappers.UserMapper;
import br.com.treinaweb.twjobs.core.repositories.UserRepository;
import br.com.treinaweb.twjobs.core.services.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid UserRequest userRequest) {
        var user = userMapper.toUser(userRequest);
        var passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(),
            loginRequest.getPassword()
        );
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return TokenResponse.builder()
            .accessToken(jwtService.generateAccessToken(loginRequest.getEmail()))
            .refreshToken(jwtService.generateRefreshToken(loginRequest.getEmail()))
            .build();
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody @Valid RefreshRequest refreshRequest) {
        var sub = jwtService.getSubFromRefreshToken(refreshRequest.getRefreshToken());
        return TokenResponse.builder()
            .accessToken(jwtService.generateAccessToken(sub))
            .refreshToken(jwtService.generateRefreshToken(sub))
            .build();
    }
    
}
