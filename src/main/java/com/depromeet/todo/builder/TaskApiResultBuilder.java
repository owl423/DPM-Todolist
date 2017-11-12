package com.depromeet.todo.builder;

import com.depromeet.todo.dto.api.TaskApiResult;
import com.depromeet.todo.dto.api.TodoApiResult;
import com.depromeet.todo.model.Task;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TaskApiResultBuilder {

    public List<TaskApiResult> buildTasksApiResult(List<Task> tasks) {

        if (CollectionUtils.isEmpty(tasks)) {
            return Collections.emptyList();
        }

        return tasks.stream()
                .map(TaskApiResult::build)
                .collect(Collectors.toList());
    }

    public TaskApiResult buildTaskApiResult(Task task) {

        return Stream.of(task)
                .map(TaskApiResult::build)
                .findFirst().orElseThrow(IllegalStateException::new);
    }
}
