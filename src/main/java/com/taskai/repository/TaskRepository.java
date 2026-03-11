package com.taskai.repository;

import com.taskai.entity.Task;
import com.taskai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByUserAndParentTaskIsNullOrderByCreatedAtDesc(User user);
    Optional<Task> findByIdAndUser(UUID id, User user);
}
