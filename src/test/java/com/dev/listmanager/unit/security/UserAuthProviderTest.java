package com.dev.listmanager.unit.security;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.entity.UserCookie;
import com.dev.listmanager.exception.InvalidCookieException;
import com.dev.listmanager.exception.UnathorizedException;
import com.dev.listmanager.security.UserAuthProvider;
import com.dev.listmanager.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class UserAuthProviderTest {

    @Mock
    private AuthService authService;

    private UserAuthProvider userAuthProvider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userAuthProvider = new UserAuthProvider(authService);
    }

    @Test
    public void testValidateCookie_ValidCookie() {
        String cookie = "validCookie";
        when(authService.findCookie(cookie)).thenReturn(Optional.of(new UserCookie()));

        Authentication result = userAuthProvider.validateCookie(cookie);

        assertTrue(result instanceof PreAuthenticatedAuthenticationToken);
    }

    @Test
    public void testValidateCookie_InvalidCookie() {
        String cookie = "invalidCookie";
        when(authService.findCookie(cookie)).thenReturn(Optional.empty());

        assertThrows(InvalidCookieException.class, () -> userAuthProvider.validateCookie(cookie));
    }

    @Test
    public void testValidateCredentials_ValidCredentials() throws UnathorizedException {
        UserDto userDto = new UserDto("username", "password");
        User user = new User("username", "password");
        when(authService.authenticate(userDto)).thenReturn(user);

        Authentication result = userAuthProvider.validateCredentials(userDto);

        assertTrue(result instanceof UsernamePasswordAuthenticationToken);
    }

    @Test
    public void testValidateCredentials_InvalidCredentials() throws UnathorizedException {
        UserDto userDto = new UserDto("username", "wrongPassword");
        when(authService.authenticate(userDto)).thenThrow(UnathorizedException.class);

        assertThrows(UnathorizedException.class, () -> userAuthProvider.validateCredentials(userDto));
    }
}