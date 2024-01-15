package org.hl.todoappbackend.entity.task;

import jakarta.persistence.*;
import org.hl.todoappbackend.entity.user.User;

import java.time.Instant;
import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Instant createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToMany(mappedBy = "tasks")
    private List<User> users;

}
