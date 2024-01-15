package org.hl.todoappbackend.entity.user;

import jakarta.persistence.*;
import org.hl.todoappbackend.entity.task.Task;

import java.util.List;

@Entity
public class User {

    @Id
    @Column(name = "username", unique = true, nullable = false, length = 255)
    private String username;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_tasks",
            joinColumns = @JoinColumn(name = "user_username"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks;

}
