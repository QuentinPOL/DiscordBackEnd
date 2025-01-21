package com.example.discordbackend.controller;

import com.example.discordbackend.model.User;
import com.example.discordbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Récupérer un utilisateur par son ID : GET /api/users/1
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }

    // Créer un nouvel utilisateur : POST /api/users
    @PostMapping("/register")
    public User registerUser(@RequestBody User userData) {
        // Pour l'exemple, on fait juste un pseudo-hash
        String hashedPwd = "HASHED_" + userData.getPasswordHash();

        return userService.createUser(
                userData.getUsername(),
                userData.getEmail(),
                hashedPwd
        );
    }
}
