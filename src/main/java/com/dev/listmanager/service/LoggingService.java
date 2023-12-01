package com.dev.listmanager.service;

import com.dev.listmanager.service.interfaces.ILoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LoggingService implements ILoggingService {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoggingService.class);

    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {}

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        StringBuilder stringBuilder = new StringBuilder();

        String method = httpServletRequest.getMethod();
        String path = httpServletRequest.getRequestURI();
        int statusCode = httpServletResponse.getStatus();
        String statusMessage = HttpStatus.valueOf(statusCode).getReasonPhrase();

        stringBuilder.append("\u001B[37m\"").append(method).append(" ").append(path).append("\"\u001B[0m ");

        // Set color based on the status code range
        if (statusCode >= 200 && statusCode < 300) {
            stringBuilder.append("\u001B[32m"); // Green color
        } else if (statusCode >= 300 && statusCode < 400) {
            stringBuilder.append("\u001B[33m"); // Yellow color
        } else if (statusCode >= 400) {
            stringBuilder.append("\u001B[31m"); // Red color
        } else {
            stringBuilder.append("\u001B[0m"); // Default color
        }

        // Append the status code and reset the color
        stringBuilder.append(statusCode).append(" ").append(statusMessage).append(" \u001B[0m"); // Reset color
        LOGGER.info(stringBuilder.toString());
    }
}
