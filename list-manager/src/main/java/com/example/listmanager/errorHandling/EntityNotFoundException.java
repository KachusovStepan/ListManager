package com.example.listmanager.errorHandling;

public class EntityNotFoundException extends RuntimeException  {
    public EntityNotFoundException(Long id) {
        super("Could not find Entity with id " + id);
    }

    public EntityNotFoundException(Long id, String name) {
        super("Could not find " + name + " with id " + id);
    }
}
