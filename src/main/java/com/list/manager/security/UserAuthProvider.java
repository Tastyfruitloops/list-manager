package com.list.manager.security;

import com.list.manager.dto.UserDto;
import com.list.manager.entities.CookieEntry;
import com.list.manager.entities.User;
import com.list.manager.exception.NotFoundException;
import com.list.manager.services.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
        Optional <CookieEntry> entry = authService.getCookie(cookie);
        System.out.println(cookie);
        System.out.println(entry);
        if (entry.isPresent()) {
            return new PreAuthenticatedAuthenticationToken(cookie, null, Collections.emptyList());
        } else throw new RuntimeException();
    }

    public Authentication validateCredentials(UserDto userLoginDTO) throws NotFoundException {
        User user = authService.authenticate(userLoginDTO);
        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }
}
