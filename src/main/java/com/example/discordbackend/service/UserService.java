package com.example.discordbackend.service;

import com.example.discordbackend.model.User;
import com.example.discordbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String username, String email, String passwordHash) {
        User user = new User(username, email, passwordHash);
        return userRepository.save(user);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    // etc... (modifier un user, supprimer, etc.)
}
