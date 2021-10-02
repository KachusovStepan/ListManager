package com.example.listmanager.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String email;
    private String password;
    @ManyToMany
    private List<Role> roles = new ArrayList<>();

    @OneToMany
    private List<ItemList> lists;

    public User() {}

    public User(Long id, String username, String email, String password, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(String name, List<Role> roles, String email) {
        this.username = name;
        this.roles = roles;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public List<ItemList> getLists() {
        return lists;
    }

    public void setLists(List<ItemList> lists) {
        this.lists = lists;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{"+ "id = " + this.id + ", name = " + this.username +
                "', roles = "+ this.roles.toString() +
                "', email = " + this.email +"'}";
    }
}