package com.dev.listmanager.controller;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.security.filter.CookieAuthFilter;
import com.dev.listmanager.service.interfaces.IAuthService;
import com.dev.listmanager.service.interfaces.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.stream.Stream;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final IUserService userService;
    private final IAuthService authService;

    @Autowired
    public AuthController(IUserService userService, IAuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserDto userDto) throws NotFoundException {
        User user = userService.getUserByUsername(userDto.getUsername());
        return getUserResponseEntity(user);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody @Valid UserDto userDto){
        User user = userService.createUser(userDto);
        return getUserResponseEntity(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        Optional<Cookie> authCookie = Stream.of(Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(cookie -> CookieAuthFilter.COOKIE_NAME.equals(cookie.getName()))
                .findFirst();
        authCookie.ifPresent(cookie -> {
            String value = cookie.getValue();
            authService.deleteCookie(value);
        });

        ResponseCookie cookie = ResponseCookie.from(CookieAuthFilter.COOKIE_NAME, "")
                .httpOnly(false)
                .sameSite("None")
                .secure(true)
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    private ResponseEntity<User> getUserResponseEntity(User user) {
        ResponseCookie cookie = ResponseCookie.from(CookieAuthFilter.COOKIE_NAME, authService.createToken(user))
                .httpOnly(false)
                .sameSite("None")
                .secure(true)
                .maxAge(1000 * 60 * 60 * 24)
                .path("/")
                .build();

        authService.addCookie(cookie.getValue());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(user);
    }

}
