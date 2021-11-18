package com.example.listmanager.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Role {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;
    private String description;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;

    public Role() {}

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Status{"+ "id = " + this.id + ", name = " + this.name +
                "', description = "+ this.description +"'}";
    }
}