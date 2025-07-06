package com.app.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.api.logging.Log4j2;
import com.app.api.service.UserService;

@RestController
@RequestMapping("/api")
public class LoginController {
	
	Log4j2 logger;
	
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        logger.info("Accessing login page.");
        return "login";
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}