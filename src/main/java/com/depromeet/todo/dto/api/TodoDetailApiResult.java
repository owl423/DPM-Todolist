package com.depromeet.todo.dto.api;

import lombok.Data;

import java.util.List;

@Data
public class TodoDetailApiResult {

    private TodoApiResult todo;
    private List<TaskApiResult> subtasks;
}
