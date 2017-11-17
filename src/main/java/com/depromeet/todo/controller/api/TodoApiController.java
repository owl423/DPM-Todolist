package com.depromeet.todo.controller.api;

import com.depromeet.todo.builder.TaskApiResultBuilder;
import com.depromeet.todo.builder.TodoApiResultBuilder;
import com.depromeet.todo.dto.api.TaskApiResult;
import com.depromeet.todo.dto.api.TodoApiResult;
import com.depromeet.todo.dto.api.TodoDetailApiResult;
import com.depromeet.todo.dto.page.TaskForm;
import com.depromeet.todo.dto.page.TodoForm;
import com.depromeet.todo.exception.InvalidTaskInfoException;
import com.depromeet.todo.exception.InvalidTodoInfoException;
import com.depromeet.todo.exception.TodoNotFoundException;
import com.depromeet.todo.model.Task;
import com.depromeet.todo.model.Todo;
import com.depromeet.todo.model.User;
import com.depromeet.todo.service.TaskService;
import com.depromeet.todo.service.TodoService;
import com.depromeet.todo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
public class TodoApiController {

    private final TodoService todoService;
    private final TaskService taskService;

    private final TodoApiResultBuilder todoApiResultBuilder;
    private final TaskApiResultBuilder taskApiResultBuilder;

    @Autowired
    public TodoApiController(TodoService todoService, TodoApiResultBuilder todoApiResultBuilder, TaskService taskService, TaskApiResultBuilder taskApiResultBuilder) {

        this.todoService = todoService;
        this.todoApiResultBuilder = todoApiResultBuilder;

        this.taskService = taskService;
        this.taskApiResultBuilder = taskApiResultBuilder;
    }

    @GetMapping("/todos")
    public ResponseEntity getTodoList(@AuthenticationPrincipal User user, @RequestParam(name = "date", required = false, defaultValue = "") String date) {

        Date time = DateUtil.parse(date);

        List<Todo> todoList = todoService.getTodos(user, time);
        return ResponseEntity.status(HttpStatus.OK).body(todoApiResultBuilder.buildTodosApiResult(todoList));
    }

    @PostMapping("/todos")
    public ResponseEntity newTodo(@AuthenticationPrincipal User user, @RequestBody @Valid TodoForm form, BindingResult result) {

        if (result.hasErrors()) {
            throw new InvalidTodoInfoException();
        }

        Todo created = todoService.newTodo(user, form);
        return ResponseEntity.status(HttpStatus.CREATED).body(todoApiResultBuilder.buildTodoApiResult(created));
    }

    @GetMapping("/todos/search")
    public ResponseEntity<List<TodoApiResult>> search(@AuthenticationPrincipal User user, @RequestParam(value = "query", required = false) String query) {

        if (StringUtils.isEmpty(query) || query.length() > 200) {
            return ResponseEntity.status(HttpStatus.OK).body(todoApiResultBuilder.buildTodosApiResult(Collections.emptyList()));
        }

        List<Todo> result = todoService.search(user, query);
        return ResponseEntity.status(HttpStatus.OK).body(todoApiResultBuilder.buildTodosApiResult(result));
    }

    @GetMapping("/todos/{todo_id}/detail")
    public ResponseEntity<TodoDetailApiResult> todoDetail(@AuthenticationPrincipal User user, @PathVariable("todo_id") Long todoId) {

        Todo todo = todoService.getTodo(user, todoId);
        List<Task> tasks = taskService.getTasks(user, todoId);

        return ResponseEntity.status(HttpStatus.OK).body(todoApiResultBuilder.buildTodoDetailApiResult(todo, tasks));
    }

    @PutMapping("/todos/{todo_id}")
    public ResponseEntity<TodoApiResult> updateTodo(@AuthenticationPrincipal User user, @PathVariable("todo_id") Long todoId, @RequestBody @Valid TodoForm form, BindingResult result) {

        if (result.hasErrors()) {
            throw new InvalidTodoInfoException();
        }

        Todo modified = todoService.modifyTodo(user, todoId, form);
        return ResponseEntity.status(HttpStatus.OK).body(todoApiResultBuilder.buildTodoApiResult(modified));
    }

    @DeleteMapping("/todos/{todo_id}")
    public ResponseEntity deleteTodo(@AuthenticationPrincipal User user, @PathVariable("todo_id") Long todoId) {

        todoService.deleteTodo(user, todoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/todos/{todo_id}/checked")
    public ResponseEntity check(@AuthenticationPrincipal User user, @PathVariable("todo_id") Long todoId) {

        todoService.changeDoneStatus(user, todoId, true);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/todos/{todo_id}/unchecked")
    public ResponseEntity uncheck(@AuthenticationPrincipal User user, @PathVariable("todo_id") Long todoId) {

        todoService.changeDoneStatus(user, todoId, false);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/todos/{todo_id}/tasks")
    public ResponseEntity<TaskApiResult> newTask(@AuthenticationPrincipal User user, @PathVariable("todo_id") Long todoId, @RequestBody @Valid TaskForm form, BindingResult result) {

        if (result.hasErrors()) {
            throw new InvalidTaskInfoException();
        }

        Task created = taskService.newTask(user, todoId, form);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskApiResultBuilder.buildTaskApiResult(created));
    }


    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity handleTodoNotFoundException() {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedException() {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler({InvalidTodoInfoException.class, InvalidTaskInfoException.class})
    public ResponseEntity handleInvalidFormInformation() {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
