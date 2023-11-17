package com.list.manager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entries")
public class ListEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostList")
    private ItemList hostList;

    @Column(name = "text")
    private String text;

    //TODO to map
    @OneToMany(mappedBy = "tag", cascade = CascadeType.PERSIST)
    private List <Tag> tagList;

    public ListEntry() {
    }

    public ListEntry(ItemList hostList, String text) {
        this.hostList = hostList;
        this.text = text;
        this.tagList = new ArrayList <>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List <Tag> getTagList() {
        return tagList;
    }

    public void addTag(Tag tag) {
        this.tagList.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tagList.remove(tag);
    }

    public Long getId() {
        return Id;
    }

    @JsonIgnore
    public ItemList getHostList() {
        return hostList;
    }
}
