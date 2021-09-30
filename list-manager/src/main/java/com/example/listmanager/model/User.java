package com.example.listmanager.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    @ManyToOne
    private Status status;
    private String email;

    public List<ItemList> getLists() {
        return lists;
    }

    public void setLists(List<ItemList> lists) {
        this.lists = lists;
    }

    @OneToMany
    private List<ItemList> lists;

    public User() {}

    public User(String name, Status status, String email) {
        this.name = name;
        this.status = status;
        this.email = email;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{"+ "id = " + this.id + ", name = " + this.name +
                "', status = "+ this.status.toString() +
                "', email = " + this.email +"'}";
    }
}
