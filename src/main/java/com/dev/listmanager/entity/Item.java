package com.dev.listmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemList_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ItemList hostList;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @JoinTable(name = "item_tags", joinColumns = @JoinColumn(name = "item_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    public Item() {
    }

    public Item(ItemList hostList, String name, List<Tag> tags) {
        this.hostList = hostList;
        this.name = name;
        this.tags = tags;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    @JsonIgnore
    public ItemList getList() {
        return hostList;
    }

    public void setList(ItemList list) {
        this.hostList = list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
