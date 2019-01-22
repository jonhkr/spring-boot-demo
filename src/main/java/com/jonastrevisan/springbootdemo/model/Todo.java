package com.jonastrevisan.springbootdemo.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Todo {
    private Long id;
    private String description;
    private boolean done;
    private Instant createdAt;
    private Instant doneAt;
    private Instant updatedAt;
}