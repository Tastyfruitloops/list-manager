package com.dev.listmanager.controller;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.security.filter.CookieAuthFilter;
import com.dev.listmanager.service.interfaces.IAuthService;
import com.dev.listmanager.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    @Operation(summary = "Login to the system using username and password")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Logged in successfully."), @ApiResponse(responseCode = "404", description = "User not found"), })
    public ResponseEntity<User> login(@RequestBody UserDto userDto) throws NotFoundException {
        User user = userService.getUserByUsername(userDto.getUsername());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, createCookie(user).toString()).body(user);
    }

    private ResponseCookie createCookie(User user) {
        ResponseCookie cookie = ResponseCookie.from(CookieAuthFilter.COOKIE_NAME, authService.createToken(user))
                .httpOnly(false).sameSite("None").secure(true).maxAge(1000 * 60 * 60 * 24).path("/").build();

        authService.addCookie(cookie.getValue());
        return cookie;
    }

    @PostMapping("/signup")
    @Operation(summary = "Create a new user")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "User created successfully."), @ApiResponse(responseCode = "400", description = "Bad request"), @ApiResponse(responseCode = "409", description = "User already exists") })
    public ResponseEntity<User> signup(@RequestBody @Valid UserDto userDto) {
        User user = userService.createUser(userDto);
        userService.createTemplateListForUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.SET_COOKIE, createCookie(user).toString())
                .body(user);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Logout successful"), @ApiResponse(responseCode = "401", description = "Unauthorized"), @ApiResponse(responseCode = "404", description = "Cookie not found"), @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();

        Optional<Cookie> authCookie = Stream.of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> CookieAuthFilter.COOKIE_NAME.equals(cookie.getName())).findFirst();
        authCookie.ifPresent(cookie -> {
            String value = cookie.getValue();
            authService.deleteCookie(value);
        });

        ResponseCookie cookie = ResponseCookie.from(CookieAuthFilter.COOKIE_NAME, "").httpOnly(false).sameSite("None")
                .secure(true).maxAge(0).path("/").build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
}
