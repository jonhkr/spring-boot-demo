package com.jonastrevisan.springbootdemo.persistence;

import com.jonastrevisan.springbootdemo.model.Todo;
import reactor.core.publisher.Mono;

public interface TodoRepository {

    Mono<Todo> save(Todo todo);

    Mono<Todo> findById(Long id);
}