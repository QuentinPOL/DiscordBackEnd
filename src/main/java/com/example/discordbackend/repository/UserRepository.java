package com.example.discordbackend.repository;

import com.example.discordbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Méthodes de recherche personnalisées
    User findByUsername(String username);
    User findByEmail(String email);
}