package com.list.manager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "tag")
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tagHostList")
    private ItemList tagHostList;

    public Tag() {
    }

    public Tag(String tag, ItemList itemList) {
        this.tag = tag;
        this.tagHostList = itemList;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @JsonIgnore
    public ItemList getTagHostList() {
        return tagHostList;
    }

    public Long getId() {
        return Id;
    }
}
