package com.example.service;

import com.example.dto.UserDTO;
import com.example.entity.Users;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Service class responsible for handling business logic related to user operations.
 */
@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;
    
    private final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * Registers a new user by validating the username and email, then storing the user details.
     *
     * @param username the username of the new user
     * @param email the email of the new user
     * @param password the password of the new user
     * @return a message indicating the result of the registration
     */
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
        // Validate username and email format
        if (!isValidUsername(username) || !isValidEmail(email)) {
            return "Invalid username or email format";
        }

        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashPassword(password)); // Hash the password
        user.setRole("USER"); // Default role for now

        userRepository.persist(user);
        return "User registered successfully!";
    }

    /**
     * Authenticates a user and returns a JWT token if the credentials are valid.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return a JWT token if authentication is successful, otherwise an error message
     */
    @Transactional
    public String login(String email, String password) {
        Optional<Users> user = userRepository.findByEmail(email);
        
        // If user does not exist or password does not match
        if (user.isEmpty() || !verifyPassword(password, user.get().getPassword())) {
            return "Invalid email or password";
        }

        // Generate JWT token (assuming JWT is configured properly)
        return Jwt.issuer("example.com")
                .subject(user.get().getUsername())
                .groups(user.get().getRole())  // Role-based access control
                .sign();
    }

    /**
     * Hashes the password using BCrypt.
     *
     * @param password the plain password to hash
     * @return the hashed password
     */
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Verifies the plain password against the hashed password.
     *
     * @param plainPassword the plain password
     * @param hashedPassword the hashed password
     * @return true if the password matches, otherwise false
     */
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /**
     * Validates the username format.
     *
     * @param username the username to validate
     * @return true if the username is valid, otherwise false
     */
    private boolean isValidUsername(String username) {
        return username != null && username.length() >= 3 && username.matches("^[a-zA-Z0-9_]+$");
    }

    /**
     * Validates the email format.
     *
     * @param email the email to validate
     * @return true if the email is valid, otherwise false
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Changes the password for a user.
     *
     * @param id the ID of the user
     * @param newPassword the new password for the user
     * @return a message indicating the result of the operation
     */
    @Transactional
    public String changePassword(Long id, String newPassword) {
        Users user = userRepository.findById(id);
        if (user == null) {
            return "User not found";
        }
        user.setPassword(hashPassword(newPassword));
        userRepository.getEntityManager().merge(user);
        return "Password updated successfully";
    }

    /**
     * Updates the role of a user.
     *
     * @param id the ID of the user
     * @param newRole the new role for the user
     * @return a message indicating the result of the operation
     */
    @Transactional
    public String updateUserRole(Long id, String newRole) {
        Users user = userRepository.findById(id);
        if (user == null) {
            return "User not found";
        }
        user.setRole(newRole);
        userRepository.getEntityManager().merge(user);
        return "User role updated successfully";
    }

    /**
     * Deactivates a user by deleting their record.
     *
     * @param id the ID of the user
     * @return a message indicating the result of the operation
     */
    @Transactional
    public String deactivateUser(Long id) {
        Users user = userRepository.findById(id);
        if (user == null) {
            return "User not found";
        }
        // Optionally set a flag to deactivate
        userRepository.delete(user);
        return "User deactivated successfully";
    }

    /**
     * Lists all users with a specific role.
     *
     * @param role the role to filter users by
     * @return a list of users with the specified role
     */
    public List<UserDTO> listUsersByRole(String role) {
        return userRepository.find("role", role).list().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds users by username.
     *
     * @param username the username to search for
     * @return a list of users with the specified username
     */
    public List<UserDTO> findUsersByUsername(String username) {
        return userRepository.find("username", username).list().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds users by email.
     *
     * @param email the email to search for
     * @return a list of users with the specified email
     */
    public List<UserDTO> findUsersByEmail(String email) {
        return userRepository.find("email", email).list().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lists all users.
     *
     * @return a list of all users
     */
    public List<UserDTO> listAllUsers() {
        return userRepository.listAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user
     * @return the user with the specified ID
     */
    public UserDTO findUserById(Long id) {
        Users user = userRepository.findById(id);
        return userMapper.toDTO(user);
    }

    /**
     * Creates a new user from a UserDTO.
     *
     * @param userDTO the user data transfer object containing user details
     * @return the created user
     */
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        Users user = userMapper.toEntity(userDTO);
        userRepository.persist(user);
        return userMapper.toDTO(user);
    }

    /**
     * Updates an existing user with the given UserDTO.
     *
     * @param userDTO the user data transfer object containing updated user details
     * @return the updated user
     */
    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        Users user = userMapper.toEntity(userDTO);
        Users updatedUser = userRepository.getEntityManager().merge(user);
        return userMapper.toDTO(updatedUser);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user
     */
    @Transactional
    public void deleteUser(Long id) {
        Users user = userRepository.findById(id);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
