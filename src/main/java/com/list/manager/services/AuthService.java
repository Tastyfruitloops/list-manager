package com.list.manager.services;

import com.list.manager.dto.UserDto;
import com.list.manager.entities.CookieEntry;
import com.list.manager.entities.User;
import com.list.manager.exception.NotFoundException;
import com.list.manager.repository.CookieRepository;
import com.list.manager.services.interfaces.IAuthService;
import com.list.manager.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class AuthService implements IAuthService {

    @Value("${auth.cookie.hmac-key:secret-key}")
    private String secretKey;

    private final IUserService userService;
    private final CookieRepository repository;

    public AuthService(IUserService userService, CookieRepository repository) {
        this.userService = userService;
        this.repository = repository;
    }

    public void addCookie(Long id, String cookie) {
        Optional <CookieEntry> entry = getCookie(cookie);
        if (entry.isEmpty()) {
            repository.save(new CookieEntry(id, cookie));
        }
    }

    public Optional <CookieEntry> getCookie(String cookie) {
        int index = cookie.indexOf('&');
        if (index == -1) {
            return Optional.empty();
        }
        Long id = Long.valueOf(cookie.substring(0, index));
        CookieEntry entry = new CookieEntry(id, cookie);
        Example <CookieEntry> example = Example.of(entry);
        List <CookieEntry> list = repository.findAll(example);
        Optional <CookieEntry> result = list.stream().findFirst();
        return result;
    }

    public void deleteCookie(String cookie) {
        Optional <CookieEntry> entry = getCookie(cookie);
        entry.ifPresent(repository::delete);
    }

    public User authenticate(UserDto userLoginDTO) throws NotFoundException {
        User reqUser = userService.getUserByUsername(userLoginDTO.getUsername());
        if (userLoginDTO.getPassword().equals(reqUser.getPassword())) {
            return reqUser;
        }
        throw new NotFoundException();
    }

    public User findByUsername(String username) throws NotFoundException {
        return userService.getUserByUsername(username);
    }

    public String createToken(User user) {
        return user.getId() + "&" + user.getUsername() + "&" + calculateHmac(user);
    }

    private String calculateHmac(User user) {
        byte[] secretKeyBytes = Objects.requireNonNull(secretKey)
                .getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = Objects.requireNonNull(user.getId() + "&" + user.getUsername())
                .getBytes(StandardCharsets.UTF_8);

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