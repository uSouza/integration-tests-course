package com.example.integrationtests.todo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public static class TodoItemNotFoundException extends RuntimeException {
        TodoItemNotFoundException() {
            super("Todo Item not found.");
        }
    }

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoItem save(TodoItem todoItem) {
        if (todoItem == null) {
            throw new IllegalArgumentException("todoItem must not be null.");
        }
        return todoRepository.save(todoItem);
    }

    public TodoItem findById(Long id) {
        return todoRepository.findById(id).orElseThrow(TodoItemNotFoundException::new);
    }

    public List<TodoItem> findAll() {
        return todoRepository.findAll();
    }

    public void delete(Long id) {
        todoRepository.delete(findById(id));
    }

    public void complete(Long id) {
        var todo = findById(id);
        todo.complete();
        todoRepository.save(todo);
    }
}
