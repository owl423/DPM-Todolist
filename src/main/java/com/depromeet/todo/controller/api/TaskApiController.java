package com.depromeet.todo.controller.api;

import com.depromeet.todo.builder.TaskApiResultBuilder;
import com.depromeet.todo.dto.api.TaskApiResult;
import com.depromeet.todo.dto.page.TaskForm;
import com.depromeet.todo.exception.InvalidTaskInfoException;
import com.depromeet.todo.exception.InvalidTodoInfoException;
import com.depromeet.todo.exception.TaskNotFoundException;
import com.depromeet.todo.exception.TodoNotFoundException;
import com.depromeet.todo.model.Task;
import com.depromeet.todo.model.User;
import com.depromeet.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TaskApiController {

    private final TaskService taskService;
    private final TaskApiResultBuilder taskApiResultBuilder;

    @Autowired
    public TaskApiController(TaskService taskService, TaskApiResultBuilder taskApiResultBuilder) {

        this.taskService = taskService;
        this.taskApiResultBuilder = taskApiResultBuilder;
    }

    @PutMapping("/tasks/{task_id}")
    public ResponseEntity<TaskApiResult> updateTask(@AuthenticationPrincipal User user, @PathVariable("task_id") Long taskId, @RequestBody @Valid TaskForm form, BindingResult result) {

        if (result.hasErrors()) {
            throw new InvalidTaskInfoException();
        }

        Task modified = taskService.modifyTask(user, taskId, form);
        return ResponseEntity.status(HttpStatus.OK).body(taskApiResultBuilder.buildTaskApiResult(modified));
    }

    @DeleteMapping("/tasks/{task_id}")
    public ResponseEntity deleteTask(@AuthenticationPrincipal User user, @PathVariable("task_id") Long taskId) {

        taskService.deleteTask(user, taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/tasks/{task_id}/checked")
    public ResponseEntity check(@AuthenticationPrincipal User user, @PathVariable("task_id") Long taskId) {

        taskService.changeDoneStatus(user, taskId, true);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/tasks/{task_id}/unchecked")
    public ResponseEntity uncheck(@AuthenticationPrincipal User user, @PathVariable("task_id") Long taskId) {

        taskService.changeDoneStatus(user, taskId, false);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/tasks/{task_id}/star")
    public ResponseEntity star(@AuthenticationPrincipal User user, @PathVariable("task_id") Long taskId) {

        taskService.changeStarStatus(user, taskId, true);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/tasks/{task_id}/unstar")
    public ResponseEntity unstar(@AuthenticationPrincipal User user, @PathVariable("task_id") Long taskId) {

        taskService.changeStarStatus(user, taskId, false);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @ExceptionHandler({TodoNotFoundException.class, TaskNotFoundException.class})
    public ResponseEntity handleItemNotFoundException() {

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
