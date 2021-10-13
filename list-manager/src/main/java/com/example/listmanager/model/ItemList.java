package com.example.listmanager.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class ItemList {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    @ManyToOne
    private Category category;

//    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> items;

    public ItemList() {}

    public ItemList(String name, Category category) {
        this.name = name;
        this.category = category;
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

    @Override
    public String toString() {
        return "ItemList{"+ "id = " + this.id + ", name = " + this.name +
                "', category = "+ this.category +"'}";
    }
}
