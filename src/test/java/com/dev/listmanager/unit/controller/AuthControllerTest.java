package com.dev.listmanager.unit.controller;

import com.dev.listmanager.controller.AuthController;
import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.service.interfaces.IAuthService;
import com.dev.listmanager.service.interfaces.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private IAuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testLogin() throws Exception {
        UserDto userDto = new UserDto("username", "password");
        User user = new User("username", "password");

        when(userService.getUserByUsername(userDto.getUsername())).thenReturn(user);
        System.out.println(objectMapper.writeValueAsString(userDto));
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("username")));
    }

    @Test
    public void testSignup() throws Exception {
        UserDto userDto = new UserDto("username", "password");
        User user = new User("username", "password");

        when(userService.createUser(userDto)).thenReturn(user);

        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))).andExpect(status().isCreated());
    }

    @Test
    public void testLogout() throws Exception {
        String cookieValue = "testCookieValue";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("token", cookieValue));

        doNothing().when(authService).deleteCookie(cookieValue);

        mockMvc.perform(post("/auth/logout").cookie(new Cookie("token", cookieValue))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        verify(authService, times(1)).deleteCookie(cookieValue);
    }
}