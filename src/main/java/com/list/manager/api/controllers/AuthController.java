package com.list.manager.api.controllers;

import com.list.manager.entities.User;
import com.list.manager.exception.NotFoundException;
import com.list.manager.exception.UnauthorizedException;
import com.list.manager.security.UserAuthProvider;
import com.list.manager.security.filter.CookieAuthFilter;
import com.list.manager.services.interfaces.IAuthService;
import com.list.manager.services.interfaces.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api")
public class AuthController {
    private final IUserService userService;
    private final IAuthService authService;
    private final UserAuthProvider userAuthProvider;

    public AuthController(IAuthService authService, IUserService userService, UserAuthProvider userAuthenticationProvider) {
        this.userService = userService;
        this.userAuthProvider = userAuthenticationProvider;
        this.authService = authService;
    }

    @RequestMapping(method = {RequestMethod.OPTIONS})
    public ResponseEntity <?> handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity <User> signIn(@RequestBody User user, HttpServletResponse response) throws UnauthorizedException, NotFoundException {
        User DB_user = userService.getUserByUsername(user.getUsername());
        ResponseCookie cookie = ResponseCookie.from(CookieAuthFilter.COOKIE_NAME, authService.createToken(DB_user))
                .httpOnly(false)
                .sameSite("None")
                .secure(false)
                .maxAge(1000 * 60 * 60 * 24)
                .path("/")
                .build();
        authService.addCookie(DB_user.getId(), cookie.getValue());
        return ResponseEntity.ok().header("token", cookie.toString()).body(DB_user);
    }

    @PostMapping("/signup")
    public ResponseEntity <User> signUp(@RequestBody @Valid User user) throws NotFoundException {
        User createdUser = userService.createUser(user);
        User DB_user = userService.getUserByUsername(user.getUsername());
        ResponseCookie cookie = ResponseCookie.from(CookieAuthFilter.COOKIE_NAME, authService.createToken(DB_user))
                .httpOnly(false)
                .sameSite("None")
                .secure(false)
                .path("/")
                .maxAge(Duration.of(1, ChronoUnit.DAYS).toSecondsPart())
                .build();

        return ResponseEntity.created(URI.create("/api/account/" + createdUser.getId())).header("token", cookie.toString()).body(createdUser);
    }

    @PostMapping("/signout")
    public ResponseEntity <Void> signOut(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        Optional <Cookie> authCookie = Stream.of(Optional.ofNullable(
                                request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(cookie -> CookieAuthFilter.COOKIE_NAME.equals(cookie.getName()))
                .findFirst();
        authCookie.ifPresent(cookie -> {
            String val = cookie.getValue();
            authService.deleteCookie(val);
        });
        return ResponseEntity.noContent().build();
    }

}
