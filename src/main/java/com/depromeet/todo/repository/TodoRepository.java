package com.depromeet.todo.repository;

import com.depromeet.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("select todo from Todo todo where user_id = :user_id and created between :time and :after order by todo.id desc")
    List<Todo> findTodos(@Param("user_id") Long userId, @Param("time") Date time, @Param("after") Date after);

    @Query("select todo from Todo todo where user_id = :user_id and title like concat('%', :query, '%') order by last_modified desc, id desc ")
    List<Todo> search(@Param("user_id") Long userId, @Param("query") String query);
}
