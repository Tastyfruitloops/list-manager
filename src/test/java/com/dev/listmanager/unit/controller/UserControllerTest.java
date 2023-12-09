package com.dev.listmanager.unit.controller;

import com.dev.listmanager.controller.UserController;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.service.interfaces.IUserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetMe() throws Exception {
        User user = new User("username", "password");
        when(userService.getUserByUsername("username")).thenReturn(user);
        mockMvc.perform(get("/users/me").cookie(new Cookie("token", "username&token"))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("username")));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(new User("user1", "password1"), new User("user2", "password2"));
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users/").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is("user1"))).andExpect((jsonPath("$[1].username", is("user2"))));
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User("username", "password");
        when(userService.getUserById("username", "1")).thenReturn(user);

        mockMvc.perform(get("/users/1").cookie(new Cookie("token", "username&token"))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("username")));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User("username", "password");
        when(userService.updateUser("username", "{\"password\":\"newPassword\"}")).thenReturn(user);

        mockMvc.perform(put("/users/me").cookie(new Cookie("token", "username&token"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"password\":\"newPassword\"}"))
                .andExpect(status().isOk()).andExpect(content().string("User was successfully updated!"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        String id = "1";
        doNothing().when(userService).deleteUser(id);

        mockMvc.perform(delete("/users/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(id);
    }

}