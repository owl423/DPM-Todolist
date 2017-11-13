package com.depromeet.todo.repository;

import com.depromeet.todo.model.Task;
import com.depromeet.todo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select task from Task task where todo_id = :todo_id")
    List<Task> findTasks(@Param("todo_id") Long todoId);
}
