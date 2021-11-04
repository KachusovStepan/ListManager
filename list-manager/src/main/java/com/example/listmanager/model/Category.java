package com.example.listmanager.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Category implements Comparable<Category> {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Category() {}
    public Category(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "ItemStatus{"+ "id = " + this.id + ", name = " + this.name + "'}";
    }

    @Override
    public int compareTo(Category other) {
        if (name == other.getName()) {
            return 0;
        }
        if (name == null) {
            return -1;
        }
        if (other.getName() == null) {
            return 1;
        }
        return name.compareTo(other.getName());
    }
}
