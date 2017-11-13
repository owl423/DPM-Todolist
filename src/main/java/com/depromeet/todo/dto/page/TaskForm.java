package com.depromeet.todo.dto.page;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class TaskForm {

    @Size(min = 1, max = 80)
    private String title;

    private Boolean done;
    private Boolean star;
}
