package com.depromeet.todo.dto.api;

import com.depromeet.todo.model.Task;
import lombok.Data;

import java.util.Date;

@Data
public class TaskApiResult {

    private Long taskId;
    private String title;
    private boolean done;
    private boolean star;

    private Date created;
    private Date lastModified;

    public static TaskApiResult build(Task task) {

        TaskApiResult result = new TaskApiResult();

        result.setTaskId(task.getId());
        result.setTitle(task.getTitle());
        result.setDone(task.isDone());
        result.setStar(task.isStar());
        result.setCreated(task.getCreated());
        result.setLastModified(task.getLastModified());

        return result;
    }
}
