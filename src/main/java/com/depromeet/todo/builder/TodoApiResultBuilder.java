package com.depromeet.todo.builder;

import com.depromeet.todo.dto.api.TodoApiResult;
import com.depromeet.todo.dto.api.TodoDetailApiResult;
import com.depromeet.todo.model.Task;
import com.depromeet.todo.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TodoApiResultBuilder {

    private final TaskApiResultBuilder builder;

    @Autowired
    public TodoApiResultBuilder(TaskApiResultBuilder builder) {

        this.builder = builder;
    }

    public List<TodoApiResult> buildTodosApiResult(List<Todo> todos) {

        if (CollectionUtils.isEmpty(todos)) {
            return Collections.emptyList();
        }

        return todos.stream()
                .map(TodoApiResult::build)
                .collect(Collectors.toList());
    }

    public TodoApiResult buildTodoApiResult(Todo todo) {

        return Stream.of(todo)
                .map(TodoApiResult::build)
                .findFirst().orElseThrow(IllegalStateException::new);
    }

    public TodoDetailApiResult buildTodoDetailApiResult(Todo todo, List<Task> tasks) {

        TodoDetailApiResult result = new TodoDetailApiResult();

        result.setTodo(buildTodoApiResult(todo));
        result.setSubtasks(builder.buildTasksApiResult(tasks));

        return result;
    }
}
