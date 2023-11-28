package com.dev.listmanager.service;

import com.dev.listmanager.service.interfaces.ILoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingService implements ILoggingService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoggingService.class);

    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {}

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("REQUEST ");
        stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
        stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
        stringBuilder.append("RESPONSE ");
        stringBuilder.append("code=").append(httpServletResponse.getStatus());

        LOGGER.info(stringBuilder.toString());
    }
}
