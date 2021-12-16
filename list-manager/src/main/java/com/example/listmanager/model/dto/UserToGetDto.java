package com.example.listmanager.model.dto;

import lombok.Data;

import java.util.List;


@Data
public class UserToGetDto {

    private Long id;

    private String username;

    private String email;

    private List<Long> roles;

    private List<Long> lists;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }

    public List<Long> getLists() {
        return lists;
    }

    public void setLists(List<Long> lists) {
        this.lists = lists;
    }
}