package com.depromeet.todo.service;

import com.depromeet.todo.dto.page.TaskForm;
import com.depromeet.todo.exception.TaskNotFoundException;
import com.depromeet.todo.model.Task;
import com.depromeet.todo.model.Todo;
import com.depromeet.todo.model.User;
import com.depromeet.todo.repository.TaskRepository;
import com.depromeet.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService extends BaseService {

    private final TodoRepository todoRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TodoRepository todoRepository, TaskRepository taskRepository) {

        this.todoRepository = todoRepository;
        this.taskRepository = taskRepository;
    }

    public Task newTask(User user, Long todoId, TaskForm form) {

        Todo todo = findOwnedTodo(todoRepository, user, todoId);

        Task task = new Task();

        task.setTitle(form.getTitle());
        task.setTodo(todo);

        return taskRepository.save(task);
    }

    public List<Task> getTasks(User user, Long todoId) {

        findOwnedTodo(todoRepository, user, todoId);
        return taskRepository.findTasks(todoId).stream()
                .sorted((o1, o2) -> {

                    boolean b1 = o1.isStar();
                    boolean b2 = o2.isStar();

                    if (b1 == b2) {

                        Date d1 = o1.getLastModified();
                        Date d2 = o2.getLastModified();

                        return d1.after(d2) ? 1 : -1;
                    }
                    else {

                        return b1 ? 1 : -1;
                    }
                })
                .collect(Collectors.toList());
    }

    public Task modifyTask(User user, Long taskId, TaskForm form) {

        Task task = findOwnedTask(user, taskId);
        task.setTitle(form.getTitle());

        if (form.getDone() != null) {
            task.setDone(form.getDone());
        }

        if (form.getStar() != null) {
            form.setStar(form.getStar());
        }

        return taskRepository.save(task);
    }

    public void deleteTask(User user, Long taskId) {

        findOwnedTask(user, taskId);
        taskRepository.delete(taskId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void changeDoneStatus(User user, Long taskId, boolean done) {

        Task task = findOwnedTask(user, taskId);
        task.setDone(done);

        taskRepository.save(task);
    }

    public void changeStarStatus(User user, Long taskId, boolean star) {

        Task task = findOwnedTask(user, taskId);
        task.setStar(star);

        taskRepository.save(task);
    }

    private Task findOwnedTask(User user, Long taskId) {

        Task task = taskRepository.findOne(taskId);

        if (task == null) {
            throw new TaskNotFoundException();
        }

        Long todoId = task.getTodo().getId();

        findOwnedTodo(todoRepository, user, todoId);
        return task;
    }
}
