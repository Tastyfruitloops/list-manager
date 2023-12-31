package com.dev.listmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 32)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Tagged List cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostList")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ItemList taggedList;

    @ManyToMany(mappedBy = "tags")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Item> items;

    public Tag() {
    }

    public Tag(String name, ItemList list) {
        this.name = name;
        this.taggedList = list;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public ItemList getList() {
        return taggedList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
