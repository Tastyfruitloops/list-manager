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

    public boolean equals(Object o) {
        if (getClass() != o.getClass()) {
            return false;
        }

        ItemListDto castedO = (ItemListDto) o;
        return castedO.getName().equals(this.name);
    }

    public String getName() {
        return name;
    }

}
