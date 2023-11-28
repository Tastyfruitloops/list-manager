package com.dev.listmanager.service;

import com.dev.listmanager.dto.UserDto;
import com.dev.listmanager.entity.User;
import com.dev.listmanager.entity.UserCookie;
import com.dev.listmanager.exception.NotFoundException;
import com.dev.listmanager.exception.UnathorizedException;
import com.dev.listmanager.repository.UserCookieRepository;
import com.dev.listmanager.repository.UserRepository;
import com.dev.listmanager.service.interfaces.IAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    public static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private static final PasswordEncoder pwEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final UserCookieRepository repository;
    @Value("${auth.cookie.hmac-key:secret-key}")
    private String secretKey;

    @Autowired
    public AuthService(UserRepository userRepository, UserCookieRepository repository) {
        this.userRepository = userRepository;
        this.repository = repository;
    }

    @Override
    public void addCookie(String cookie) {
        Optional<UserCookie> optionalCookie = repository.findByCookie(cookie);
        if (optionalCookie.isEmpty()) {
            repository.save(new UserCookie(cookie));
        }
    }

    @Override
    public void deleteCookie(String cookie) {
        Optional<UserCookie> optionalCookie = repository.findByCookie(cookie);
        optionalCookie.ifPresent(repository::delete);
    }

    @Override
    public Optional<UserCookie> findCookie(String cookie) {
        return repository.findByCookie(cookie);
    }

    @Override
    public User authenticate(UserDto userDto) throws UnathorizedException {
        Optional<User> user = userRepository.findByUsername(userDto.getUsername());
        if (user.isPresent()) {
            if (pwEncoder.matches(userDto.getPassword(), user.get().getHashedPassword())) {
                LOGGER.debug("User {} authenticated", userDto.getUsername());
                return user.get();
            } else {
                LOGGER.debug("User {} tried to authenticate with wrong password", userDto.getUsername());
                throw new UnathorizedException(String.format("User %s authenticated with wrong password", userDto.getUsername()));
            }
        } else {
            LOGGER.debug("Non-existing user {} tried to authenticate", userDto.getUsername());
            throw new UnathorizedException(String.format("User %s not found", userDto.getUsername()));
        }

    }

    @Override
    public User findByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    @Override
    public String createToken(User user) {
        return user.getUsername() + "&" + calculateHmac(user);
    }

    private String calculateHmac(User user) {
        byte[] secretKeyBytes = Objects.requireNonNull(secretKey).getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = user.getUsername().getBytes(StandardCharsets.UTF_8);

        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec sec = new SecretKeySpec(secretKeyBytes, "HmacSHA512");
            mac.init(sec);
            byte[] hmacBytes = mac.doFinal(valueBytes);
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
