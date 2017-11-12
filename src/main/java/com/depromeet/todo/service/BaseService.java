package com.depromeet.todo.service;

import com.depromeet.todo.exception.TodoNotFoundException;
import com.depromeet.todo.model.Todo;
import com.depromeet.todo.model.User;
import com.depromeet.todo.repository.TodoRepository;
import org.springframework.security.access.AccessDeniedException;

public class BaseService {

    protected Todo findOwnedTodo(TodoRepository todoRepository, User user, Long todoId) {

        Todo todo = todoRepository.findOne(todoId);

        if (todo == null) {
            throw new TodoNotFoundException();
        }

        if (!todo.getUser().equals(user)) {
            throw new AccessDeniedException("");
        }

        return todo;
    }
}
