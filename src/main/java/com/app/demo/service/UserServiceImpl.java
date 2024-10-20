package com.app.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.app.demo.exception.DuplicateFieldException;
import com.app.demo.exception.RecordNotFoundException;
import com.app.demo.exception.InvalidUpdateException; // New custom exception for invalid updates
import com.app.demo.model.User;
import com.app.demo.model.UserStatus;
import com.app.demo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE);
    }

    // Save a new user (Create)
    @Override
    public User save(User user) throws DuplicateFieldException {
        // Check if username or email exists with any status
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new DuplicateFieldException("Username already exists.");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new DuplicateFieldException("Email already exists.");
        }

        // Encode password and set user status to ACTIVE
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }

    // Find a user by ID (Read)
    public Optional<User> findById(Long userId) {
        return userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE);
    }

    // Get all users (excluding DELETED)
    public List<User> findAllUsers() {
        return userRepository.findAllByStatus(UserStatus.ACTIVE); // Only return active users
    }

    // Update an existing user (Update)
    public User updateUser(Long userId, User userDetails) throws DuplicateFieldException, InvalidUpdateException {
        Optional<User> userOptional = userRepository.findById(userId);

        // If user not found, throw a custom exception
        if (!userOptional.isPresent()) {
            throw new RecordNotFoundException("No record found for the given user's id.");
        }

        User existingUser = userOptional.get();

        // Check if the user's status is not ACTIVE, throw an exception
        if (!existingUser.getStatus().equals(UserStatus.ACTIVE)) {
            throw new InvalidUpdateException("Updation is not allowed for this user.");
        }

        // Check for duplicate username or email, excluding the current user
        User userWithSameUsername = userRepository.findByUsername(userDetails.getUsername());
        User userWithSameEmail = userRepository.findByEmail(userDetails.getEmail());

        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(existingUser.getId())) {
            throw new DuplicateFieldException("Username already exists.");
        }
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(existingUser.getId())) {
            throw new DuplicateFieldException("Email already exists.");
        }

        // Update fields
        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setName(userDetails.getName());

        // If password is provided, encode and update it
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    // Soft delete user (mark as DELETED)
    @Override
    public void delete(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setStatus(UserStatus.DELETED);
            existingUser.setDeletedAt(LocalDateTime.now());
            userRepository.save(existingUser);
        } else {
            throw new RecordNotFoundException("User not found");
        }
    }

    // Permanent delete user (if required)
    public void deletePermanently(Long userId) {
        userRepository.deleteById(userId);
    }
}