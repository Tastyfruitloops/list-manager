package com.dev.listmanager.security;

import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.service.ItemListService;
import com.dev.listmanager.service.UserService;
import com.dev.listmanager.util.RequestWrapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class UserAccessProviderTest {

    @Mock
    private UserService userService;

    @Mock
    private ItemListService itemListService;

    private RequestWrapper requestWrapper;

    @InjectMocks
    private UserAccessProvider userAccessProvider;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws IOException, NotFoundException {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userAccessProvider).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void authenticatedUserCanViewLists() throws IOException {

        User testUser = new User("testuser", "password");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "lists/1");
        request.setCookies(new Cookie("token", "testuser&123"));
        requestWrapper = new RequestWrapper(request);
        when(userService.getUserByUsernameOptional(anyString())).thenReturn(Optional.of(testUser));

        boolean result = userAccessProvider.canViewLists(requestWrapper);
        Assertions.assertTrue(result);
    }

    @Test
    void notAuthenticatedUserCanNotViewLists() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "lists");
        request.setCookies(new Cookie("token", "testuser&123"));
        request.setServletPath("lists/1");
        requestWrapper = new RequestWrapper(request);
        when(userService.getUserByUsernameOptional("testuser")).thenReturn(Optional.empty());

        boolean result = userAccessProvider.canViewLists(requestWrapper);
        Assertions.assertFalse(result);
    }

    @Test
    void OwnerCanModifyItem() throws IOException, NotFoundException {
        User testUser = new User("testuser", "password");
        ItemList testList = new ItemList("testList", testUser);
        Item item = new Item(testList, "item", new ArrayList<>());

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "lists");
        request.setCookies(new Cookie("token", "testuser&123"));
        request.setServletPath("lists/item/1");
        requestWrapper = new RequestWrapper(request);
        when(userService.getUserByUsernameOptional(anyString())).thenReturn(Optional.of(testUser));
        when(itemListService.getItemById(anyString())).thenReturn(item);


        boolean result = userAccessProvider.canModifyItem(requestWrapper);
        Assertions.assertTrue(result);
    }

    @Test
    void ownerCanModifyTag() throws IOException, NotFoundException {
        User testUser = new User("testuser", "password");
        ItemList testList = new ItemList("testList", testUser);
        Tag tag = new Tag("tag", testList);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "lists");
        request.setCookies(new Cookie("token", "testuser&123"));
        request.setServletPath("lists/tag/1");
        requestWrapper = new RequestWrapper(request);
        when(userService.getUserByUsernameOptional(anyString())).thenReturn(Optional.of(testUser));
        when(itemListService.getTagById(anyString())).thenReturn(tag);


        boolean result = userAccessProvider.canModifyTag(requestWrapper);
        Assertions.assertTrue(result);
    }

    @Test
    void userWithAccessCanAccessList() throws IOException, NotFoundException {
        User testUser = new User("testuser", "password");
        ItemList testList = new ItemList("testList", testUser);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "lists");
        request.setCookies(new Cookie("token", "testuser&123"));
        request.setServletPath("lists/1");
        requestWrapper = new RequestWrapper(request);
        when(userService.getUserByUsernameOptional(anyString())).thenReturn(Optional.of(testUser));
        when(itemListService.getListById(anyString())).thenReturn(testList);


        boolean result = userAccessProvider.canAccessList(requestWrapper);
        Assertions.assertTrue(result);
    }

    @Test
    void userCanAccessPublicList() throws IOException, NotFoundException {
        User testUser = new User("testuser", "password");
        User otherUser = new User("otherUser", "otherPass");
        ItemList testList = new ItemList("testList", otherUser);
        testList.setPublic(true);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "lists");
        request.setCookies(new Cookie("token", "testuser&123"));
        request.setServletPath("lists/1");
        requestWrapper = new RequestWrapper(request);
        when(userService.getUserByUsernameOptional(anyString())).thenReturn(Optional.of(testUser));
        when(itemListService.getListById(anyString())).thenReturn(testList);


        boolean result = userAccessProvider.canAccessList(requestWrapper);
        Assertions.assertTrue(result);
    }

    @Test
    void ownerCanModifyList() throws IOException, NotFoundException {
        User testUser = new User("testuser", "password");
        ItemList testList = new ItemList("testList", testUser);

        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "lists");
        request.setCookies(new Cookie("token", "testuser&123"));
        request.setServletPath("lists/1");
        requestWrapper = new RequestWrapper(request);
        when(userService.getUserByUsernameOptional(anyString())).thenReturn(Optional.of(testUser));
        when(itemListService.getListById(anyString())).thenReturn(testList);


        boolean result = userAccessProvider.canModifyList(requestWrapper);
        Assertions.assertTrue(result);
    }

    @Test
    void canAccessListsThrowsExceptionAndReturnsFalse() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "lists");
        request.setServletPath("lists/1");
        request.setCookies(new Cookie("null", "null"));

        requestWrapper = new RequestWrapper(request);
        Assertions.assertFalse(userAccessProvider.canAccessList(requestWrapper));
    }

    @Test
    void canViewListsThrowsExceptionAndReturnsFalse() throws IOException, NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "lists");
        request.setServletPath("lists/1");
        request.setCookies(new Cookie("null", "null"));

        requestWrapper = new RequestWrapper(request);

        when(itemListService.getListById(anyString())).thenThrow(new NotFoundException());


        Assertions.assertFalse(userAccessProvider.canViewLists(requestWrapper));

    }

    @Test
    void canModifyListsThrowsExceptionAndReturnsFalse() throws IOException, NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "lists");
        request.setServletPath("lists/1");
        request.setCookies(new Cookie("null", "null"));

        when(itemListService.getListById(anyString())).thenThrow(new NotFoundException());

        requestWrapper = new RequestWrapper(request);
        Assertions.assertFalse(userAccessProvider.canModifyList(requestWrapper));
    }

    @Test
    void canModifyItemsThrowsExceptionAndReturnsFalse() throws IOException, NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "lists");
        request.setServletPath("lists/item/1");
        request.setCookies(new Cookie("null", "null"));

        when(itemListService.getItemById(anyString())).thenThrow(new NotFoundException());


        requestWrapper = new RequestWrapper(request);
        Assertions.assertFalse(userAccessProvider.canModifyItem(requestWrapper));
    }

    @Test
    void canModifyTagsThrowsExceptionAndReturnsFalse() throws IOException, NotFoundException {
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "lists");
        request.setServletPath("lists/tag/1");
        request.setCookies(new Cookie("null", "null"));

        when(itemListService.getTagById(anyString())).thenThrow(new NotFoundException());

        requestWrapper = new RequestWrapper(request);
        Assertions.assertFalse(userAccessProvider.canModifyTag(requestWrapper));
    }


}