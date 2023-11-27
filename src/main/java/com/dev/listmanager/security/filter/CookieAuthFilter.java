package com.dev.listmanager.security.filter;

import com.dev.listmanager.security.UserAuthProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class CookieAuthFilter extends OncePerRequestFilter {
    public static final String COOKIE_NAME = "token";
    private final UserAuthProvider authProvider;

    public CookieAuthFilter(UserAuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> cookieAuth = Stream.of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0])).filter(cookie -> COOKIE_NAME.equals(cookie.getName())).findFirst();

        cookieAuth.ifPresent(cookie -> {
                    try {
                        SecurityContextHolder.getContext().setAuthentication(authProvider.validateCookie(cookie.getValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        );
        filterChain.doFilter(request, response);
    }
}
