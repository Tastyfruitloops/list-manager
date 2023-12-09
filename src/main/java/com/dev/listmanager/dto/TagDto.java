package com.dev.listmanager.dto;

import jakarta.validation.constraints.Pattern;

public class TagDto {
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Name can only contain alphanumeric characters")
    private String name;

    public TagDto() {
    }

    public TagDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
