package com.depromeet.todo.dto.page;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class TodoForm {

    @Size(min = 1, max = 200)
    private String title;

    private Boolean done;
}
