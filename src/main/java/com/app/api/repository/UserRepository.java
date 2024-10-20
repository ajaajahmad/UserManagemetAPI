package com.app.api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.api.model.User;
import com.app.api.model.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByUsernameAndStatus(String username, UserStatus status);
    User findByEmailAndStatus(String email, UserStatus status);
    List<User> findAllByStatus(UserStatus status);
    Optional<User> findByIdAndStatus(Long id, UserStatus status);

    // Find users that are not soft deleted (deleted_at is null)
    List<User> findByDeletedAtIsNull();

    // Find a user by ID, ensuring soft-deleted users are excluded
    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    // Optional: Add method to find all users excluding soft-deleted ones
    List<User> findAllByDeletedAtIsNull();

    // New methods to check for duplicates without filtering by status
    User findByUsername(String username);
    User findByEmail(String email);
}