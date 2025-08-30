package com.healthtracker.service;

import com.healthtracker.model.User;
import com.healthtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        
        // If user not found, create a default user (for demo purposes)
        // In a real application, this should handle the case appropriately
        User defaultUser = new User();
        defaultUser.setUsername(username);
        defaultUser.setEmail(username + "@example.com");
        defaultUser.setRoles(Collections.singletonList("PATIENT"));
        defaultUser.setId(generateUserId());
        
        // Save the user to database
        return userRepository.save(defaultUser);
    }

    public User getUserById(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User createUser(String username, String email, String password, String role) {
        User user = new User();
        user.setId(generateUserId());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // In real app, this should be encrypted
        user.setRoles(Collections.singletonList(role));
        
        return userRepository.save(user);
    }

    private String generateUserId() {
        return "user_" + System.currentTimeMillis();
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
