package com.list.manager.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.list.manager.dto.UserDto;
import com.list.manager.exception.NotFoundException;
import com.list.manager.security.UserAuthProvider;
import com.list.manager.util.RequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class UserPresenceFilter extends OncePerRequestFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final UserAuthProvider authProvider;
    private final BasicJsonParser parser = new BasicJsonParser();

    public UserPresenceFilter(UserAuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        RequestWrapper request1 = new RequestWrapper(request);
        if (request1.getServletPath().startsWith("/api/login")
                && HttpMethod.POST.matches((request1.getMethod()))) {
            Map <String, Object> dto = parser.parseMap(request1.getBody());
            UserDto userLoginDTO = new UserDto((String) dto.get("username"), (String) dto.get("password"));
            try {
                SecurityContextHolder.getContext().setAuthentication(
                        authProvider.validateCredentials(userLoginDTO)
                );
            } catch (RuntimeException e) {
                SecurityContextHolder.clearContext();
                throw e;
            } catch (NotFoundException e) {
                SecurityContextHolder.clearContext();
                e.printStackTrace();
            }
        }
        filterChain.doFilter(request1, response);
    }
}

