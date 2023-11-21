package com.dev.listmanager.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password can only contain alphanumeric characters")
    @Size(min = 8)
    private String password;

    public UserDto() {
    }

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
