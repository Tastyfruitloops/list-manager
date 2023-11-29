package com.dev.listmanager.security.filter;

import com.dev.listmanager.security.UserAccessProvider;
import com.dev.listmanager.util.RequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AccessFilter extends OncePerRequestFilter {
    private final UserAccessProvider accessProvider;

    public AccessFilter(UserAccessProvider userAccessProvider) {
        this.accessProvider = userAccessProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RequestWrapper requestCopy = new RequestWrapper(request);

        if (HttpMethod.GET.matches(requestCopy.getMethod())) {
            String servletPath = requestCopy.getServletPath();
            if (servletPath.startsWith("/lists")) {
                if (servletPath.equals("/lists/") && !accessProvider.canViewLists(requestCopy)) {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                } else if (!servletPath.equals("/lists/") && !accessProvider.canAccessList(requestCopy)) {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            }
        }

        if (HttpMethod.PUT.matches(requestCopy.getMethod()) || HttpMethod.DELETE.matches(request.getMethod()) || HttpMethod.POST.matches((request.getMethod()))) {
            String servletPath = requestCopy.getServletPath();

            if (servletPath.startsWith("/lists")) {
                if (servletPath.startsWith("/lists/item")) {
                    if (!accessProvider.canModifyItem(requestCopy)) {
                        SecurityContextHolder.clearContext();
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    }
                } else if (servletPath.startsWith("/lists/tag")) {
                    if (!accessProvider.canModifyTag(requestCopy)) {
                        SecurityContextHolder.clearContext();
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    }
                } else if (!accessProvider.canModifyList(requestCopy)) {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            }
        }

        filterChain.doFilter(requestCopy, response);
    }
}
