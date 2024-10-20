package com.app.api.service;

import java.util.List;
import java.util.Optional;

import com.app.api.exception.DuplicateFieldException;
import com.app.api.model.User;

public interface UserService {
    
    // Find by username
    User findByUsername(String username);

    // Find by email
    User findByEmail(String email);

    // Save user
    User save(User user) throws DuplicateFieldException;

    // Soft delete user by marking as DELETED and setting deletedAt timestamp
    void delete(Long userId);

    // Find user by ID
    Optional<User> findById(Long userId);

    // Get all users excluding DELETED ones
    List<User> findAllUsers();

    // Update user details
    User updateUser(Long userId, User userDetails) throws DuplicateFieldException;

    // Permanently delete user (if needed)
    void deletePermanently(Long userId);
}