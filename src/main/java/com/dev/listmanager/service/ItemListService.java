package com.dev.listmanager.service;

import com.dev.listmanager.dto.ItemDto;
import com.dev.listmanager.dto.ItemListDto;
import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.exception.UnathorizedException;
import com.dev.listmanager.repository.ItemListRepository;
import com.dev.listmanager.repository.ItemRepository;
import com.dev.listmanager.repository.TagRepository;
import com.dev.listmanager.repository.UserRepository;
import com.dev.listmanager.service.interfaces.IItemListService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemListService implements IItemListService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ItemListService.class);
    private final ItemListRepository repository;
    private final ItemRepository itemRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final BasicJsonParser parser = new BasicJsonParser();

    @Autowired
    public ItemListService(ItemListRepository repository, ItemRepository itemRepository, TagRepository tagRepository, UserRepository userRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    public Item addItem(String uuid, ItemDto itemDto) {
        List<Tag> tags = itemDto.getTagIds().stream()
                .map(tagId -> tagRepository.findById(UUID.fromString(tagId)).orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());

        ItemList list = repository.findById(UUID.fromString(uuid)).orElseThrow(IllegalArgumentException::new);

        Item item = new Item(list, itemDto.getName(), tags);
        list.addItem(item);
        repository.save(list);
        LOGGER.debug("Item {} added to list {}", item.getId(), list.getId());
        return itemRepository.save(item);
    }

    public void deleteItem(String uuid) throws NotFoundException {
        Item item = itemRepository.findById(UUID.fromString(uuid)).orElseThrow(NotFoundException::new);
        ItemList list = item.getList();
        list.removeItem(item);
        LOGGER.debug("Item {} removed from list {}", item.getId(), list.getId());
        itemRepository.delete(item);
    }

    public Item updateItem(String uuid, String attributes) {
        Optional<Item> optionalItem = itemRepository.findById(UUID.fromString(uuid));
        Item item = optionalItem.orElseThrow(RuntimeException::new);

        JSONObject jsonObject = new JSONObject(attributes);
        if (jsonObject.has("name")) {
            item.setName(jsonObject.getString("name"));
        }

        if (jsonObject.has("tagIds")) {
            JSONArray tagsArray = jsonObject.getJSONArray("tagIds");
            List<String> tagIds = new ArrayList<>();
            for (int i = 0; i < tagsArray.length(); i++) {
                tagIds.add(tagsArray.getString(i));
            }
            List<Tag> tags = tagIds.stream()
                    .map(tagId -> tagRepository.findById(UUID.fromString(tagId)).orElseThrow(RuntimeException::new))
                    .collect(Collectors.toList());

            item.setTags(tags);
        }
        LOGGER.debug("Item {} updated", item.getId());
        return itemRepository.save(item);
    }

    public Tag addTag(String uuid, String name) throws NotFoundException {
        Optional<ItemList> optionalItemList = repository.findById(UUID.fromString(uuid));
        ItemList list = optionalItemList.orElseThrow(NotFoundException::new);

        Tag tag = new Tag(name, list);
        list.addTag(tag);
        LOGGER.debug("Tag {} added to list {}", tag.getId(), list.getId());
        return tagRepository.save(tag);
    }

    public void deleteTag(String uuid) throws NotFoundException {
        Tag tag = tagRepository.findById(UUID.fromString(uuid)).orElseThrow(NotFoundException::new);
        ItemList list = tag.getList();
        list.removeTag(tag);
        LOGGER.debug("Tag {} removed from list {}", tag.getId(), list.getId());
        tagRepository.delete(tag);
    }

    public Tag updateTag(String uuid, String name) throws NotFoundException {
        Optional<Tag> optionalTag = tagRepository.findById(UUID.fromString(uuid));
        Tag tag = optionalTag.orElseThrow(NotFoundException::new);

        tag.setName(name);
        LOGGER.debug("Tag {} updated", tag.getId());
        return tagRepository.save(tag);
    }

    @Override
    public List<ItemList> getAllLists() {
        return repository.findAll();
    }

    @Override
    public ItemList getListById(String id) throws IllegalArgumentException, NotFoundException {
        UUID uuid = UUID.fromString(id);
        Optional<ItemList> list = repository.findById(uuid);
        return list.orElseThrow(NotFoundException::new);
    }

    @Override
    public Tag getTagById(String id) throws IllegalArgumentException, NotFoundException {
        UUID uuid = UUID.fromString(id);
        Optional<Tag> tag = tagRepository.findById(uuid);
        return tag.orElseThrow(NotFoundException::new);
    }

    @Override
    public ItemList getListByName(String name) throws NotFoundException {
        Optional<ItemList> list = repository.findByName(name);
        return list.orElseThrow(NotFoundException::new);
    }

    @Override
    public Item getItemByName(String name) throws NotFoundException {
        Optional<Item> item = itemRepository.findByName(name);
        return item.orElseThrow(NotFoundException::new);
    }

    @Override
    public Item getItemById(String id) throws NotFoundException {
        UUID uuid = UUID.fromString(id);
        Optional<Item> item = itemRepository.findById(uuid);
        return item.orElseThrow(NotFoundException::new);
    }

    @Override
    public List<ItemList> getListsByUserId(String userId) {
        UUID uuid = UUID.fromString(userId);
        Optional<User> user = userRepository.findById(uuid);
        if (user.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return repository.findAllByOwner(user.get());
    }

    @Override
    public List<ItemList> getListsByCookie(String cookie) throws UnathorizedException, NotFoundException {
        String username = cookie.split("&")[0];
        if (username.isEmpty()) {
            throw new UnathorizedException();
        }

        User owner = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
        return repository.findAllByOwner(owner);
    }

    @Override
    public ItemList createList(String cookie, ItemListDto itemListDto) throws UnathorizedException, NotFoundException {
        String username = cookie.split("&")[0];
        if (username.isEmpty()) {
            throw new UnathorizedException();
        }
        var owner = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
        var itemList = new ItemList(itemListDto.getName(), owner);
        LOGGER.debug("List {} created, owner {}", itemList.getId(), owner.getId());
        return repository.save(itemList);
    }

    @Override
    public ItemList updateList(String id, String attributes) throws NotFoundException {
        Optional<ItemList> optionalItemList = repository.findById(UUID.fromString(id));
        ItemList list = optionalItemList.orElseThrow(NotFoundException::new);

        Map<String, Object> attributesMap = parser.parseMap(attributes);
        attributesMap.forEach((key, value) -> {
            switch (key) {
                case "name" -> list.setName((String) value);
                case "public" -> list.setPublic(Boolean.parseBoolean((String) value));
                case "archived" -> list.setArchived(Boolean.parseBoolean((String) value));
            }
        });
        LOGGER.debug("List {} updated", list.getId());
        return repository.save(list);
    }

    @Override
    public void deleteList(String id) throws NotFoundException {
        ItemList itemList = repository.findById(UUID.fromString(id)).orElseThrow(NotFoundException::new);
        LOGGER.debug("List {} deleted", id);
        repository.delete(itemList);
    }
}
