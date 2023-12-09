package com.dev.listmanager.unit.service;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.repository.ItemListRepository;
import com.dev.listmanager.repository.ItemRepository;
import com.dev.listmanager.repository.TagRepository;
import com.dev.listmanager.repository.UserRepository;
import com.dev.listmanager.service.UserService;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemListRepository itemListRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ValidatorFactory validatorFactory;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Arrays.asList(new User("user1", "password1"), new User("user2", "password2"));
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById() throws NotFoundException {
        String id = UUID.randomUUID().toString();
        User user = new User("username", "password");
        when(userRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(user));

        User result = userService.getUserById("username", id);

        assertEquals(user, result);
        verify(userRepository, times(1)).findById(UUID.fromString(id));
    }

    @Test
    public void testGetUserByIdNotFound() {
        String id = UUID.randomUUID().toString();
        when(userRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById("username", id));
        verify(userRepository, times(1)).findById(UUID.fromString(id));
    }

    @Test
    public void testGetUserByUsername() throws NotFoundException {
        User user = new User("username", "password");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername("username");

        assertEquals(user, result);
        verify(userRepository, times(1)).findByUsername("username");
    }

    @Test
    public void testGetUserByUsernameNotFound() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserByUsername("username"));
        verify(userRepository, times(1)).findByUsername("username");
    }

    @Test
    public void testCreateUser() {
        UserDto userDto = new UserDto("username", "password");
        User user = new User("username", "password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Validator validator = mock(Validator.class);
        when(validatorFactory.getValidator()).thenReturn(validator);
        when(validator.validate(user)).thenReturn(Collections.emptySet());
        when(validator.validate(userDto)).thenReturn(Collections.emptySet());

        User result = userService.createUser(userDto);

        assertEquals(user, result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser() throws NotFoundException {
        User user = new User("username", "password");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser("username", "{\"password\":\"newPassword\"}");

        assertEquals(user, result);
        verify(userRepository, times(1)).findByUsername("username");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testDeleteUser() throws NotFoundException {
        String id = UUID.randomUUID().toString();
        User user = new User("username", "password");
        when(userRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(user));

        userService.deleteUser(id);

        verify(userRepository, times(1)).findById(UUID.fromString(id));
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testCreateTemplateListForUser() {
        User user = new User("username", "password");
        ItemList sampleList = new ItemList("My favourite movies", user);
        when(itemListRepository.save(any(ItemList.class))).thenReturn(sampleList);

        userService.createTemplateListForUser(user);

        verify(itemListRepository, times(1)).save(any(ItemList.class));
    }
}