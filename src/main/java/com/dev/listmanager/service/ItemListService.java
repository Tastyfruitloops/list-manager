package com.dev.listmanager.service;

import com.dev.listmanager.dto.ItemDto;
import com.dev.listmanager.dto.ItemListDto;
import com.dev.listmanager.entity.*;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.exception.UnathorizedException;
import com.dev.listmanager.repository.*;
import com.dev.listmanager.service.interfaces.IItemListService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemListService implements IItemListService {
    private final ItemListRepository repository;
    private final ItemRepository itemRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final BasicJsonParser parser = new BasicJsonParser();

    @Autowired
    public ItemListService(ItemListRepository repository, ItemRepository itemRepository, TagRepository tagRepository, UserRepository userRepository, UserCookieRepository userCookieRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    public Item addItem(String uuid, ItemDto itemDto) {
        List<Tag> tags = itemDto.getTagIds().stream()
                .map(tagId -> tagRepository.findById(UUID.fromString(tagId))
                        .orElseThrow(() -> new RuntimeException("Tag not found with ID: " + tagId)))
                .collect(Collectors.toList());

        ItemList list = repository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new RuntimeException("ItemList not found with ID: " + uuid));

        Item item = new Item(list,itemDto.getName(), tags);
        list.addItem(item);
        repository.save(list);
        return itemRepository.save(item);
    }

    public void deleteItem(String uuid) throws NotFoundException {
        Item item = itemRepository.findById(UUID.fromString(uuid)).orElseThrow(NotFoundException::new);
        ItemList list = item.getList();
        list.removeItem(item);
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
                    .map(tagId -> tagRepository.findById(UUID.fromString(tagId))
                            .orElseThrow(RuntimeException::new))
                    .collect(Collectors.toList());

            item.setTags(tags);
        }
        return itemRepository.save(item);
    }

    public Tag addTag(String uuid, String name) throws NotFoundException {
        Optional<ItemList> optionalItemList = repository.findById(UUID.fromString(uuid));
        ItemList list = optionalItemList.orElseThrow(NotFoundException::new);

        Tag tag = new Tag(name, list);
        list.addTag(tag);
        return tagRepository.save(tag);
    }

    public void deleteTag(String uuid) throws NotFoundException {
        Tag tag = tagRepository.findById(UUID.fromString(uuid)).orElseThrow(NotFoundException::new);
        ItemList list = tag.getList();
        list.removeTag(tag);
        tagRepository.delete(tag);
    }

    public Tag updateTag(String uuid, String name) throws NotFoundException{
        Optional<Tag> optionalTag = tagRepository.findById(UUID.fromString(uuid));
        Tag tag = optionalTag.orElseThrow(NotFoundException::new);

        tag.setName(name);
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
    public Tag getTagById(String id) throws IllegalArgumentException, NotFoundException{
        UUID uuid = UUID.fromString(id);
        Optional<Tag> tag = tagRepository.findById(uuid);
        return tag.orElseThrow(NotFoundException::new);
    }
    @Override
    public ItemList getListByName(String name) throws NotFoundException{
        Optional<ItemList> list = repository.findByName(name);
        return list.orElseThrow(NotFoundException::new);
    }
    @Override
    public Item getItemById(String id) throws NotFoundException{
        UUID uuid = UUID.fromString(id);
        Optional<Item> item = itemRepository.findById(uuid);
        return item.orElseThrow(NotFoundException::new);
    }
    @Override
    public Item getItemByName(String name) throws NotFoundException{
        Optional<Item> item = itemRepository.findByName(name);
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
    public ItemList createList(String cookie, ItemListDto itemListDto) throws UnathorizedException {
        String username = cookie.split("&")[0];
        if (username.isEmpty()){
            throw new UnathorizedException();
        }
        var owner = userRepository.findByUsername(username).get();
        var itemList = new ItemList(itemListDto.getName(),owner);

        return repository.save(itemList);
    }

    @Override
    public ItemList updateList(String id, String attributes) {
        Optional<ItemList> optionalItemList = repository.findById(UUID.fromString(id));
        ItemList list = optionalItemList.orElseThrow(IllegalArgumentException::new);

        Map<String, Object> attributesMap = parser.parseMap(attributes);
        attributesMap.forEach((key, value) -> {
            switch (key) {
                case "name" -> list.setName((String) value);
                case "public" -> list.setPublic(Boolean.parseBoolean((String) value));
                case "archived" -> list.setArchived(Boolean.parseBoolean((String) value));
            }
        });

        return repository.save(list);
    }

    @Override
    public void deleteList(String id) throws NotFoundException {
        ItemList itemList = repository.findById(UUID.fromString(id)).orElseThrow(NotFoundException::new);
        repository.delete(itemList);
    }
}
