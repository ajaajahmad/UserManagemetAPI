package com.app.api.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.app.api.exception.DuplicateFieldException;
import com.app.api.model.LoginRequest;
import com.app.api.model.User;
import com.app.api.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Register user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        logger.info("Attempting to register user with username: {}", user.getUsername());
        try {
            User savedUser = userService.save(user);
            logger.info("User registration successful for username: {}", savedUser.getUsername());
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (DuplicateFieldException e) {
            logger.warn("DuplicateFieldException: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException while registering user: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Attempting login for username: {}", loginRequest.getUsername());
        User user = userService.findByUsername(loginRequest.getUsername());

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.info("Login successful for username: {}", loginRequest.getUsername());
            return ResponseEntity.ok("Login successful");
        } else {
            logger.warn("Login failed for username: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // Retrieve a user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        logger.info("Fetching user details for ID: {}", id);
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            logger.info("User found for ID: {}", id);
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            logger.warn("User not found for ID: {}", id);
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    // Retrieve all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Update user
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        logger.info("Updating user with ID: {}", id);
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            logger.info("User updated successfully for ID: {}", id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (DuplicateFieldException e) {
            logger.warn("DuplicateFieldException while updating user: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException while updating user: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Soft delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Soft deleting user with ID: {}", id);
        userService.delete(id);
        logger.info("User soft deleted successfully for ID: {}", id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    // Permanently delete user
    @DeleteMapping("/users/{id}/permanent")
    public ResponseEntity<?> deleteUserPermanently(@PathVariable Long id) {
        logger.info("Permanently deleting user with ID: {}", id);
        userService.deletePermanently(id);
        logger.info("User permanently deleted for ID: {}", id);
        return new ResponseEntity<>("User permanently deleted", HttpStatus.OK);
    }
}