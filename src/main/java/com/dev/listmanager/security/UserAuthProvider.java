package com.dev.listmanager.security;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.entity.UserCookie;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.service.AuthService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Optional;

@Component
public class UserAuthProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;
    private final AuthService authService;


    public UserAuthProvider(AuthService authService) {
        this.authService = authService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Authentication validateCookie(String cookie) {
        Optional<UserCookie> optionalCookie = authService.findCookie(cookie);
        if (optionalCookie.isPresent()) {
            return new PreAuthenticatedAuthenticationToken(cookie, null, Collections.emptyList());
        } else throw new RuntimeException("Invalid cookie");
    }

    public Authentication validateCredentials(UserDto userDto) throws NotFoundException {
        User user = authService.authenticate(userDto);
        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }
}