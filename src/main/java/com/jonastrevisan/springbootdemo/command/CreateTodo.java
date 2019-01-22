package com.jonastrevisan.springbootdemo.command;

import com.jonastrevisan.springbootdemo.model.Todo;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateTodo {

    @NotBlank
    private String description;
    private boolean done;

    public Todo toTodo() {
        Todo todo = new Todo();
        todo.setDescription(this.getDescription());
        todo.setDone(this.isDone());

        return todo;
    }
}