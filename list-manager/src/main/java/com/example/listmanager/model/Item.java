package com.example.listmanager.model;


import javax.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue
    private Long id;
    private int number;
    private int priority = 1;
    private String description;
    @ManyToOne
    private ItemStatus status;

    public Item() {}
    public Item(int number, String description) {
        this.number = number;
        this.description = description;
        this.priority = 0;
    }

    public Item(int number, int priority, String description, ItemStatus status) {
        this.number = number;
        this.priority = priority;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Item{"+ "id = " + this.id + ", number = " + this.number +
                ", priority = " + this.priority + ", description = " +
                this.description + ", status = " + this.status + "'}";
    }
}
