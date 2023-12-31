package com.dev.listmanager.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lists")
public class ItemList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 32)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Owner cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User owner;

    @Column(name = "public", nullable = false)
    private boolean isPublic;

    @Column(name = "archived", nullable = false)
    private boolean isArchived;

    @OneToMany(mappedBy = "hostList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    @OneToMany(mappedBy = "taggedList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags;

    public ItemList() {
    }

    public ItemList(String name, User owner) {
        this.name = name;
        this.owner = owner;
        this.isPublic = false;
        this.isArchived = false;

        this.items = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    @JsonProperty
    public UUID getOwnerId() {
        return owner.getId();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }
}
