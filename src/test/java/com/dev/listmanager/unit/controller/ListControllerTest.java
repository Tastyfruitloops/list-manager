package com.dev.listmanager.unit.controller;

import com.dev.listmanager.controller.ListController;
import com.dev.listmanager.dto.TagDto;
import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.service.interfaces.IItemListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ListControllerTest {

    @Mock
    private IItemListService itemListService;

    @InjectMocks
    private ListController listController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(listController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetMyLists() throws Exception {
        User user = new User("username", "password");
        List<ItemList> lists = Arrays.asList(new ItemList("List1", user), new ItemList("List2", user));
        when(itemListService.getListsByCookie("username&token")).thenReturn(lists);

        mockMvc.perform(get("/lists/").cookie(new Cookie("token", "username&token"))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(lists.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(lists.get(1).getName())));
    }

    @Test
    public void testGetById() throws Exception {
        User user = new User("username", "password");
        ItemList list = new ItemList("List1", user);
        when(itemListService.getListById("1")).thenReturn(list);

        mockMvc.perform(get("/lists/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(list.getName())));
    }


    @Test
    public void testTagList() throws Exception {
        TagDto tagDto = new TagDto("Tag1");
        Tag tag = new Tag("Tag1", null);
        when(itemListService.addTag("1", tagDto.getName())).thenReturn(tag);
        System.out.println(objectMapper.writeValueAsString(tagDto));
        mockMvc.perform(post("/lists/1/tag").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(tag.getName())));
    }

    @Test
    public void testUpdateList() throws Exception {
        User user = new User("username", "password");
        String attributes = "{\"name\":\"newName\"}";
        ItemList list = new ItemList("List1", user);
        when(itemListService.updateList("1", attributes)).thenReturn(list);


        mockMvc.perform(put("/lists/1").contentType(MediaType.APPLICATION_JSON).content(attributes))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name", is(list.getName())));
    }

    @Test
    public void testUpdateItem() throws Exception {
        String attributes = "{\"name\":\"newName\"}";
        Item item = new Item(new ItemList(), "Test Item", new ArrayList<>());
        when(itemListService.updateItem("1", attributes)).thenReturn(item);

        mockMvc.perform(put("/lists/item/1").contentType(MediaType.APPLICATION_JSON).content(attributes))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name", is(item.getName())));
    }

    @Test
    public void testUpdateTag() throws Exception {
        TagDto tagDto = new TagDto("newName");
        Tag tag = new Tag("newName", null);
        when(itemListService.updateTag("1", tagDto.getName())).thenReturn(tag);

        mockMvc.perform(put("/lists/tag/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(tag.getName())));
    }

    @Test
    public void testDeleteList() throws Exception {
        mockMvc.perform(delete("/lists/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteItem() throws Exception {
        mockMvc.perform(delete("/lists/item/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteTag() throws Exception {
        mockMvc.perform(delete("/lists/tag/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}