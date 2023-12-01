package com.dev.listmanager.service;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.Item;
import com.dev.listmanager.entity.ItemList;
import com.dev.listmanager.entity.Tag;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.repository.ItemListRepository;
import com.dev.listmanager.repository.ItemRepository;
import com.dev.listmanager.repository.TagRepository;
import com.dev.listmanager.repository.UserRepository;
import com.dev.listmanager.service.interfaces.IUserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;
    private final ItemListRepository itemListRepository;
    private final ItemRepository itemRepository;
    private final TagRepository tagRepository;
    private final BasicJsonParser parser = new BasicJsonParser();
    private final ValidatorFactory validatorFactory;

    @Autowired
    public UserService(UserRepository repository, ItemListRepository itemListRepository, ItemRepository itemRepository, TagRepository tagRepository, ValidatorFactory validatorFactory) {
        this.repository = repository;
        this.itemListRepository = itemListRepository;
        this.itemRepository = itemRepository;
        this.tagRepository = tagRepository;
        this.validatorFactory = validatorFactory;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(String requesterUsername, String id) throws NotFoundException {
        Optional<User> optionalUser = repository.findById(UUID.fromString(id));

        User user = optionalUser.orElseThrow(NotFoundException::new);

        if (!user.getUsername().equals(requesterUsername)) {
            user.setLists(user.getLists().stream().filter(itemList -> itemList.isPublic() && !itemList.isArchived())
                    .collect(Collectors.toList()));
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) throws NotFoundException {
        Optional<User> user = repository.findByUsername(username);
        return user.orElseThrow(NotFoundException::new);
    }

    @Override
    public User createUser(UserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getPassword());

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Set<ConstraintViolation<UserDto>> passwordViolations = validator.validate(userDto);

        if (!violations.isEmpty() || !passwordViolations.isEmpty()) {
            throw new IllegalArgumentException(violations.toString());
        }
        LOGGER.debug("User {} created", user.getId());
        return repository.save(user);
    }

    @Override
    public User updateUser(String username, String attributes) throws NotFoundException {
        User user = repository.findByUsername(username).orElseThrow(NotFoundException::new);

        Map<String, Object> map = parser.parseMap(attributes);
        map.forEach((s, o) -> {
            if (s.equals("password")) {
                user.setPassword(user.getHashedPassword());
            }
        });

        LOGGER.debug("User {} updated", user.getId());
        return repository.save(user);
    }

    @Override
    public void deleteUser(String id) throws NotFoundException {
        User user = repository.findById(UUID.fromString(id)).orElseThrow(NotFoundException::new);
        LOGGER.debug("User {} deleted", user.getId());
        repository.delete(user);
    }

    @Override
    public void createTemplateListForUser(User user) {
        ItemList sampleList = new ItemList("My favourite movies", user);
        itemListRepository.save(sampleList);

        ArrayList<String> sampleTagNames = new ArrayList<>(Arrays.asList("Drama",
                "Action",
                "Crime",
                "Sci-Fi",
                "Comedy",
                "Romance",
                "Thriller"
        ));
        ArrayList<Tag> tagList = new ArrayList<>();
        sampleTagNames.forEach(tagName -> {
            Tag tag = new Tag(tagName, sampleList);
            tagList.add(tag);
            tagRepository.save(tag);
            sampleList.addTag(tag);
        });

        List<Item> sampleItems = Arrays.asList(new Item(sampleList,
                        "The Shawshank Redemption",
                        List.of(tagList.get(0))
                ),
                new Item(sampleList, "The Dark Knight", List.of(tagList.get(0), tagList.get(1), tagList.get(2))),
                new Item(sampleList, "Inception", List.of(tagList.get(3), tagList.get(1), tagList.get(6))),
                new Item(sampleList, "Forrest Gump", List.of(tagList.get(4), tagList.get(0), tagList.get(5))),
                new Item(sampleList, "The Matrix", List.of(tagList.get(3), tagList.get(1)))
        );
        sampleItems.forEach(item -> {
            itemRepository.save(item);
            sampleList.addItem(item);
        });

        user.addList(sampleList);
    }

    public Optional<User> getUserByUsernameOptional(String username) {
        return repository.findByUsername(username);
    }
}
