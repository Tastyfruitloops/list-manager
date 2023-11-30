package com.dev.listmanager.security;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.entity.UserCookie;
import com.dev.listmanager.exception.InvalidCookieException;
import com.dev.listmanager.exception.UnathorizedException;
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

    private final AuthService authService;
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;


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
        } else {
            throw new InvalidCookieException();
        }
    }

    public Authentication validateCredentials(UserDto userDto) throws UnathorizedException {
        User user = authService.authenticate(userDto);
        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }
}