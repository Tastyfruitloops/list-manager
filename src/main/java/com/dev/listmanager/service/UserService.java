package com.dev.listmanager.service;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.repository.UserRepository;
import com.dev.listmanager.service.interfaces.IUserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements IUserService {
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
    public User getUserById(String id) throws NotFoundException {
        Optional<User> user = repository.findById(UUID.fromString(id));
        return user.orElseThrow(NotFoundException::new);
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

        return repository.save(optionalUser.get());
    }

    @Override
    public void deleteUser(String id) throws NotFoundException {
        User user = repository.findById(UUID.fromString(id)).orElseThrow(NotFoundException::new);
        repository.delete(user);
    }
}
