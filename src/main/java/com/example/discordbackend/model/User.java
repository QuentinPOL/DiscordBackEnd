package com.example.discordbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")  // correspond à la table "users" dans la BDD
public class User {

    // Pour l'id auto-incrémenté
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // On stocke ici le hash du mot de passe (pas le mot de passe en clair)
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    // Constructeur vide (obligatoire pour JPA)
    public User() {
    }

    // Constructeur A laisser
    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // Getters et setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
