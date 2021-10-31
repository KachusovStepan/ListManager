package com.example.listmanager.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class ItemList {
    @Id
    @GeneratedValue
    @Column(name = "item_list_id")
    private Long id;

    private String name;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User user;

    @ManyToOne
    private Category category;

//    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> items;

    public ItemList() {}

    public ItemList(String name, User user, Category category, List<Item> items) {
        this.name = name;
        this.category = category;
        this.items = items;
        this.user = user;
    }

    public ItemList(String name, Category category, List<Item> items) {
        this.name = name;
        this.category = category;
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "ItemList{"+ "id = " + this.id + ", name = " + this.name +
                "', category = "+ this.category +"'}";
    }
}
