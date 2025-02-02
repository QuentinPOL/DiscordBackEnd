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
    @Column(name = "password_hash", nullable = false, length = 150)
    private String password;

    @Column(name = "avatar", nullable = false, length = 3000)
    private String avatar = "https://www.pngmart.com/files/22/User-Avatar-Profile-Download-PNG-Isolated-Image.png";

    @Column(name = "description", nullable = true, length = 180)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status = Status.ONLINE; // Statut par défaut : En ligne


    public User() {
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Constructeur
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters et setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String passwordHash) { this.password = passwordHash; }
}
