package com.example.listmanager.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ItemStatus {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public ItemStatus() {}

    public ItemStatus(String name) {
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
}
