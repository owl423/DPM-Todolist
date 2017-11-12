package com.depromeet.todo.controller.api;

import com.depromeet.todo.builder.UserApiResultBuilder;
import com.depromeet.todo.dto.api.UserApiResult;
import com.depromeet.todo.dto.page.UserForm;
import com.depromeet.todo.exception.InvalidUserInfoException;
import com.depromeet.todo.model.User;
import com.depromeet.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserApiController {

    private final UserService userService;

    private final UserApiResultBuilder builder;

    @Autowired
    public UserApiController(UserService userService, UserApiResultBuilder builder) {

        this.userService = userService;
        this.builder = builder;
    }

    @PostMapping("/register")
    public ResponseEntity<UserApiResult> register(@RequestBody @Valid UserForm form, BindingResult result) {

        if (result.hasErrors()) {
            throw new InvalidUserInfoException();
        }

        User created = userService.register(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(builder.buildUserApiResult(created));
    }

    @ExceptionHandler(InvalidUserInfoException.class)
    public ResponseEntity handleInvalidUserInformation() {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleConflictAccount() {

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

}
