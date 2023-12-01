package com.dev.listmanager.security.filter;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.exception.InternalAuthenticationException;
import com.dev.listmanager.exception.UnathorizedException;
import com.dev.listmanager.security.UserAuthProvider;
import com.dev.listmanager.util.RequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class UserPresenceFilter extends OncePerRequestFilter {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserPresenceFilter.class);
    private final UserAuthProvider authProvider;
    private final BasicJsonParser parser = new BasicJsonParser();

    public UserPresenceFilter(UserAuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RequestWrapper request1 = new RequestWrapper(request);
        if (request1.getServletPath().startsWith("/auth/login") && HttpMethod.POST.matches((request1.getMethod()))) {
            Map<String, Object> dto = parser.parseMap(request1.getBody());
            UserDto userLoginDTO = new UserDto((String) dto.get("username"), (String) dto.get("password"));
            try {
                SecurityContextHolder.getContext().setAuthentication(authProvider.validateCredentials(userLoginDTO));
            } catch (RuntimeException e) {
                SecurityContextHolder.clearContext();
                LOGGER.error("User {} not authenticated", userLoginDTO.getUsername());
                throw new InternalAuthenticationException(e);
            } catch (UnathorizedException e) {
                SecurityContextHolder.clearContext();
                LOGGER.error("User {} not authenticated", userLoginDTO.getUsername());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request1, response);
    }
}
