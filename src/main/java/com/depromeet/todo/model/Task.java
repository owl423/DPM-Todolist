package com.depromeet.todo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "subtasks")
public class Task {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean star;
    private boolean done;

    private Date created;

    @Column(name = "last_modified")
    private Date lastModified;


    @ManyToOne
    @JoinColumn(name = "todo_id", referencedColumnName = "id")
    private Todo todo;


    @PrePersist
    protected void onCreate() {

        created = new Date();
        lastModified = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = new Date();
    }
}
