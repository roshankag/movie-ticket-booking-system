package com.example.service;

import com.example.entity.Users;
import com.example.repository.UserRepository;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    // Registration method
    @Transactional
    public String register(String username, String email, String password) {
        // Check if username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            return "Username already exists";
        }
        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            return "Email already exists";
        }

        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashPassword(password)); // Hash the password
        user.setRole("USER"); // Default role for now

        userRepository.persist(user);
        return "User registered successfully!";
    }

    // Login method that returns JWT token if credentials are correct
    public String login(String email, String password) {
        Optional<Users> user = userRepository.findByEmail(email);
        if (user.isEmpty() || !verifyPassword(password, user.get().getPassword())) {
            return "Invalid email or password";
        }

        // Generate JWT token
        return Jwt.issuer("example.com")
                .subject(user.get().getUsername())
                .groups(user.get().getRole())  // Role-based access control
                .sign();
    }

    // Utility method to hash password using BCrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Utility method to verify password
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public List<Users> listAllUsers() {
        return userRepository.listAll();
    }

    public Users findUserById(Long id) {
        return userRepository.findById(id);
    }

    public String createUser(Users user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists";
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already exists";
        }
        
        userRepository.persist(user);
        return "User created successfully!";
    }

    public String updateUser(Users user) {
        Users existingUser = userRepository.findById(user.getId());
        if (existingUser == null) {
            return "User not found";
        }

        return userRepository.getEntityManager().merge(user) != null ? "User updated successfully!" : "Update failed";
    }

    public String deleteUser(Long id) {
        Users user = userRepository.findById(id);
        if (user == null) {
            return "User not found";
        }
        
        userRepository.delete(user);
        return "User deleted successfully!";
    }
}
