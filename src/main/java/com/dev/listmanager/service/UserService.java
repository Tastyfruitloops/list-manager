package com.dev.listmanager.service;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.NotFoundException;
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
    private final BasicJsonParser parser = new BasicJsonParser();
    private final ValidatorFactory validatorFactory;

    @Autowired
    public UserService(UserRepository repository, ValidatorFactory validatorFactory) {
        this.repository = repository;
        this.validatorFactory = validatorFactory;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(String requesterUsername, String id) throws NotFoundException {
        Optional<User> user = repository.findById(UUID.fromString(id));

        User dbUser = user.orElseThrow(NotFoundException::new);

        if (!dbUser.getUsername().equals(requesterUsername)) {
            dbUser.setLists(dbUser.getLists().stream().filter(itemList -> itemList.isPublic() && !itemList.isArchived()).collect(Collectors.toList()));
        }
        return dbUser;
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
        Optional<User> optionalUser = repository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException();
        }
        optionalUser.ifPresent(user -> {
            Map<String, Object> map = parser.parseMap(attributes);
            map.forEach((s, o) -> {
                switch (s) {
                    case "password" -> user.setPassword(user.getHashedPassword());
                }
            });
        });
        LOGGER.debug("User {} updated", optionalUser.get().getId());
        return repository.save(optionalUser.get());
    }

    @Override
    public void deleteUser(String id) throws NotFoundException {
        User user = repository.findById(UUID.fromString(id)).orElseThrow(NotFoundException::new);
        LOGGER.debug("User {} deleted", user.getId());
        repository.delete(user);
    }

    public Optional<User> getUserByUsernameOptional(String username) {
        return repository.findByUsername(username);
    }
}
