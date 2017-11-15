package com.depromeet.todo.service;

import com.depromeet.todo.dto.page.TodoForm;
import com.depromeet.todo.model.Task;
import com.depromeet.todo.model.Todo;
import com.depromeet.todo.model.User;
import com.depromeet.todo.repository.TodoRepository;
import com.depromeet.todo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class TodoService extends BaseService {

    private final TodoRepository todoRepository;
    private final TaskService taskService;

    @Autowired
    public TodoService(TodoRepository todoRepository, TaskService taskService) {

        this.todoRepository = todoRepository;
        this.taskService = taskService;
    }

    public Todo newTodo(User user, TodoForm form) {

        Todo todo = new Todo();

        todo.setTitle(form.getTitle());
        todo.setUser(user);

        return todoRepository.save(todo);
    }

    public List<Todo> getTodos(User user, Date time) {

        Date after = DateUtil.add(time, 24 * 60 * 60 * 1000);
        return todoRepository.findTodos(user.getId(), time, after);
    }

    public Todo getTodo(User user, Long todoId) {

        return findOwnedTodo(todoRepository, user, todoId);
    }

    public List<Todo> search(User user, String query) {
        return todoRepository.search(user.getId(), query);
    }

    public Todo modifyTodo(User user, Long todoId, TodoForm form) {

        Todo todo = findOwnedTodo(todoRepository, user, todoId);
        todo.setTitle(form.getTitle());

        if (form.getDone() != null) {
            todo.setDone(form.getDone());
        }

        return todoRepository.save(todo);
    }

    public void deleteTodo(User user, Long todoId) {

        findOwnedTodo(todoRepository, user, todoId);
        todoRepository.delete(todoId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void changeDoneStatus(User user, Long todoId, boolean done) {

        Todo todo = findOwnedTodo(todoRepository, user, todoId);
        todo.setDone(done);

        todoRepository.save(todo);

        List<Task> tasks = todo.getTasks();

        if (!CollectionUtils.isEmpty(tasks)) {

            for (Task task : tasks) {
                taskService.changeDoneStatus(user, task.getId(), done);
            }
        }
    }
}
