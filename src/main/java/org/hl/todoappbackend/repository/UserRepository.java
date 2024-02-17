package org.hl.todoappbackend.repository;

import org.hl.todoappbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
