package com.dev.listmanager.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lists")
public class ItemList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

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

    public User getOwner() {
        return owner;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }
}
