package com.dev.listmanager.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    private static final PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username can only contain alphanumeric characters")
    private String username;

    @Column(name = "password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ItemList> lists = new ArrayList<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = pwEncoder.encode(password);
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getHashedPassword() {
        return password;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<ItemList> getLists() {
        return lists;
    }

    public void setLists(List<ItemList> lists) {
        this.lists = lists;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public List<ItemList> getPublicLists() {
        return lists.stream().filter(ItemList::isPublic).collect(Collectors.toList());
    }

    public void addList(ItemList list) {
        this.lists.add(list);
    }

    public void setPassword(String password) {
        this.password = pwEncoder.encode(password);
    }
}
