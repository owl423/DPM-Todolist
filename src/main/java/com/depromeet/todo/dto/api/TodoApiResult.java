package com.depromeet.todo.dto.api;

import com.depromeet.todo.model.Task;
import com.depromeet.todo.model.Todo;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Date;

@Data
public class TodoApiResult {

    private Long todoId;
    private String title;
    private boolean done;

    private Date created;
    private Date lastModified;

    private long totalTasks;
    private long doneTasks;

    public static TodoApiResult build(Todo todo) {

        TodoApiResult result = new TodoApiResult();

        result.todoId = todo.getId();
        result.title = todo.getTitle();
        result.created = todo.getCreated();
        result.lastModified = todo.getLastModified();

        if (CollectionUtils.isEmpty(todo.getTasks())) {

            result.done = todo.isDone();
        }
        else {

            result.totalTasks = todo.getTasks().size();
            result.doneTasks = todo.getTasks().stream().filter(Task::isDone).count();

            result.done = result.totalTasks == result.doneTasks;
        }

        return result;
    }
}
