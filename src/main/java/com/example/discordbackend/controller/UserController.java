package com.example.discordbackend.controller;
import com.example.discordbackend.dto.LoginRequest;
import com.example.discordbackend.model.User;
import com.example.discordbackend.security.HashUtil;
import com.example.discordbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.discordbackend.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;


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
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User userData) {
        // Hasher le mot de passe avec SHA-256
        String hashedPwd = HashUtil.hashPassword(userData.getPassword());

        User newUser = userService.createUser(
                userData.getUsername(),
                userData.getEmail(),
                hashedPwd
        );

        // Génération du token JWT pour l'utilisateur nouvellement inscrit
        String token = JwtUtil.generateToken(newUser.getEmail());

        // Construire la réponse JSON
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", newUser.getUsername());
        response.put("email", newUser.getEmail());
        response.put("status", newUser.getStatus().name());
        response.put("avatar", newUser.getAvatar());

        return ResponseEntity.ok(response);
    }

    // Endpoint pour changer le statut d'un utilisateur
    @PatchMapping("/status")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, String> request) {
        String status = request.get("status");

        try {
            userService.updateStatus(status);
            return ResponseEntity.ok("Statut mis à jour avec succès : " + status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            boolean authenticated = userService.authenticate(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());

            if (authenticated) {
                // Trouver l'utilisateur (par email ou username)
                User user;
                if (loginRequest.getUsernameOrEmail().contains("@")) {
                    user = userService.findByEmail(loginRequest.getUsernameOrEmail());
                } else {
                    user = userService.findByUsername(loginRequest.getUsernameOrEmail());
                }

                // Générer un token JWT
                String token = JwtUtil.generateToken(user.getEmail());

                // Construire la réponse JSON
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("username", user.getUsername());
                response.put("email", user.getEmail());
                response.put("status", user.getStatus().name());
                response.put("avatar", user.getAvatar());

                return ResponseEntity.ok(response);
            }
        } catch (IllegalArgumentException e) {
            // L'exception sera interceptée par le `GlobalExceptionHandler`
            throw new IllegalArgumentException(e.getMessage());
        }

        // Normalement, cette ligne ne sera jamais atteinte
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Erreur inconnue");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }



        @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // Vérifie si le token commence bien par "Bearer "
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Retire "Bearer "
            }

            String email = JwtUtil.validateToken(token);
            return ResponseEntity.ok("Token valide pour l'utilisateur : " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide ou expiré");
        }
    }

}
