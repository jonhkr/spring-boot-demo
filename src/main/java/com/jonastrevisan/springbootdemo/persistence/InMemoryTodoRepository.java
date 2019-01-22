package com.jonastrevisan.springbootdemo.persistence;

import com.jonastrevisan.springbootdemo.model.Todo;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTodoRepository implements TodoRepository {

    private final ConcurrentHashMap<Long, Todo> store = new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1L);

    @Override
    public Mono<Todo> save(Todo todo) {
        Assert.notNull(todo, "Can't save 'null'");

        if (todo.getId() != null) {
            if (!store.containsKey(todo.getId())) {
                throw new IllegalArgumentException("Could not find todo with id [" + todo.getId() + "]");
            }

            todo.setUpdatedAt(Instant.now());
        } else {
            todo.setId(idGenerator.incrementAndGet());
            todo.setCreatedAt(Instant.now());
        }

        if (todo.isDone() && todo.getDoneAt() == null) {
            todo.setDoneAt(Instant.now());
        }

        store.put(todo.getId(), todo);

        return Mono.just(todo);
    }

    @Override
    public Mono<Todo> findById(Long id) {
        if (store.containsKey(id)) {
            return Mono.just(store.get(id));
        }

        return Mono.empty();
    }
}