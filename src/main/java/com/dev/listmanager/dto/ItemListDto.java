package com.dev.listmanager.dto;

import jakarta.validation.constraints.Pattern;

public class ItemListDto {
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Name can only contain alphanumeric characters")
    private String name;

    public ItemListDto() {
    }

    public ItemListDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
