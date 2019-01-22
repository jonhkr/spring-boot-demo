package com.jonastrevisan.springbootdemo.controller;

import com.jonastrevisan.springbootdemo.command.CreateTodo;
import com.jonastrevisan.springbootdemo.model.Todo;
import com.jonastrevisan.springbootdemo.persistence.TodoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> create(@RequestBody @Valid Mono<CreateTodo> command) {
        return command.flatMap((createTodo -> todoRepository.save(createTodo.toTodo())))
                .map(todo -> ResponseEntity.created(buildTodoUri(todo)).build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Todo>> byId(@PathVariable Long id) {
        return todoRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    private URI buildTodoUri(Todo todo) {
        return UriComponentsBuilder.fromPath("/api/v1/todos/{id}")
                .buildAndExpand(todo.getId())
                .toUri();
    }
}