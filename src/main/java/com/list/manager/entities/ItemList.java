package com.list.manager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lists")
public class ItemList {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "shared")
    private boolean shared;

    @Column(name = "archived")
    private boolean archived;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "hostList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <ListEntry> entryList;

    //TODO to map
    @OneToMany(mappedBy = "tagHostList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <Tag> listTags;

    public ItemList() {
    }

    public ItemList(String name, User owner) {
        this.id = uuidToLong(UUID.randomUUID());
        this.owner = owner;
        this.name = name;
        this.shared = false;
        this.archived = false;
        this.entryList = new ArrayList <>();
        this.listTags = new ArrayList <>();
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public User getOwner() {
        return owner;
    }

    public List <ListEntry> getEntryList() {
        return entryList;
    }


    public void addEntry(ListEntry entry) {
        this.entryList.add(entry);
    }

    public void deleteEntry(ListEntry entry) {
        this.entryList.remove(entry);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public List <Tag> getListTags() {
        return listTags;
    }

    public void addTag(Tag tag) {
        this.listTags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.listTags.remove(tag);
    }

    public static Long uuidToLong(UUID uuid) {
        return uuid.getMostSignificantBits();
    }
}
