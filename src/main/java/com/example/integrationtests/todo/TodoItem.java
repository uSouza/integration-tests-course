package com.example.integrationtests.todo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class TodoItem {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String category;
    private boolean done;

    public TodoItem(String name, String description, String category, boolean done) {
        Objects.requireNonNull(name, "Name must not be null.");
        Objects.requireNonNull(description, "Description must not be null.");

        this.name = name;
        this.description = description;
        this.category = category;
        this.done = done;
    }

    public TodoItem(String name, String description) {
        this(name, description, "Undefined", false);
    }

    public TodoItem(Long id, String name, String description) {
        this(name, description, "Undefined", false);
        this.setId(id);
    }

    public TodoItem() {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isDone() {
        return done;
    }

    public void complete() {
        this.done = true;
    }
}
