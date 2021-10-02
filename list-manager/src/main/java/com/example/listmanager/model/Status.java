package com.example.listmanager.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Status {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    public Status() {}

    public Status(String name, String description) {
        this.name = name;
        this.description = description;
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
