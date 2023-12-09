package com.dev.listmanager.unit.service;

import com.dev.listmanager.service.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class LoggingServiceTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private LoggingService loggingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @ParameterizedTest
    @ValueSource(ints = { 200, 300, 400, 500 })
    public void testLogResponseWithDifferentStatusCodes(int statusCode) {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/test");
        when(response.getStatus()).thenReturn(statusCode);

        loggingService.logResponse(request, response, null);

        verify(request, times(1)).getMethod();
        verify(request, times(1)).getRequestURI();
        verify(response, times(1)).getStatus();
    }
}