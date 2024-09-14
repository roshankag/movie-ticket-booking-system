package com.example.service;

import com.example.entity.Users;
import com.example.repository.UserRepository;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public List<Users> listAllUsers() {
        return userRepository.listAll();
    }

    public Users findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Users createUser(Users user) {
        userRepository.persist(user);
        return user;
    }

    public Users updateUser(Users user) {
        return userRepository.getEntityManager().merge(user);
    }

    public void deleteUser(Long id) {
        Users user = userRepository.findById(id);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
