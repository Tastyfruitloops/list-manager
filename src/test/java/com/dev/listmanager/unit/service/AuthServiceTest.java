package com.dev.listmanager.unit.service;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.entity.UserCookie;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.exception.UnathorizedException;
import com.dev.listmanager.repository.UserCookieRepository;
import com.dev.listmanager.repository.UserRepository;
import com.dev.listmanager.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCookieRepository userCookieRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(authService, "secretKey", "test-secret-key");
    }

    @Test
    public void testAddCookie() {
        String cookie = "testCookie";
        when(userCookieRepository.findByCookie(cookie)).thenReturn(Optional.empty());

        authService.addCookie(cookie);

        verify(userCookieRepository, times(1)).save(any(UserCookie.class));
    }

    @Test
    public void testDeleteCookie() {
        String cookie = "testCookie";
        UserCookie userCookie = new UserCookie(cookie);
        when(userCookieRepository.findByCookie(cookie)).thenReturn(Optional.of(userCookie));

        authService.deleteCookie(cookie);

        verify(userCookieRepository, times(1)).delete(userCookie);
    }

    @Test
    public void testFindCookie() {
        String cookie = "testCookie";
        UserCookie userCookie = new UserCookie(cookie);
        when(userCookieRepository.findByCookie(cookie)).thenReturn(Optional.of(userCookie));

        Optional<UserCookie> result = authService.findCookie(cookie);

        assertTrue(result.isPresent());
        assertEquals(cookie, result.get().getCookie());
    }

    @Test
    public void testAuthenticate() throws UnathorizedException {
        String username = "testUser";
        String password = "testPassword";
        UserDto userDto = new UserDto(username, password);
        User user = new User(username, password);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = authService.authenticate(userDto);

        assertEquals(username, result.getUsername());
    }

    @Test
    public void testFindByUsername() throws NotFoundException {
        String username = "testUser";
        User user = new User(username, "testPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = authService.findByUsername(username);

        assertEquals(username, result.getUsername());
    }

    @Test
    public void testCreateToken() {
        String username = "testUser";
        String password = "testPassword";
        User user = new User(username, password);

        String result = authService.createToken(user);

        assertNotNull(result);
        assertTrue(result.startsWith(username));
    }

    @Test
    public void testAuthenticateThrowsUnathorizedException() {
        String username = "testUser";
        String password = "testPassword";
        String wrongPassword = "wrongPassword";
        User user = new User(username, password);
        UserDto userDto = new UserDto(username, wrongPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(UnathorizedException.class, () -> authService.authenticate(userDto));
    }
}