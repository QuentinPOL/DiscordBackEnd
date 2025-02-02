package com.example.discordbackend.service;

import com.example.discordbackend.model.Status;
import com.example.discordbackend.model.User;
import com.example.discordbackend.repository.UserRepository;
import com.example.discordbackend.security.CurrentUserUtils;
import com.example.discordbackend.security.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private static final String SECRET_KEY = "0572b69695bc312a3fb1d94d78a48932171f7afb28a5d06b667804e8605888f2";
/*
    public String generateToken(String email) {
        return JwtUtil.generateToken(email);
    }
*/

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateStatus(String status) {
        String userEmail = CurrentUserUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));

        try {
            Status newStatus = Status.valueOf(status.toUpperCase());
            user.setStatus(newStatus);
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide. Les statuts valides sont : ONLINE, IDLE, DO_NOT_DISTURB, INVISIBLE.");
        }
    }

    public boolean authenticate(String usernameOrEmail, String password) {
        Optional<User> userOptional;

        // Vérifie si c'est un email ou un username
        if (usernameOrEmail.contains("@")) {
            userOptional = userRepository.findByEmail(usernameOrEmail);
        } else {
            userOptional = userRepository.findByUsername(usernameOrEmail);
        }

        // Vérifie si l'utilisateur existe
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur introuvable.");
        }

        User user = userOptional.get();

        // Hasher le mot de passe fourni et comparer avec celui stocké
        String hashedPassword = HashUtil.hashPassword(password);
        if (!user.getPassword().equals(hashedPassword)) {
            throw new IllegalArgumentException("Mot de passe incorrect.");
        }

        return true; // Authentification réussie
    }




    public User createUser(String username, String email, String password) {
        // Vérifier si le username existe déjà
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Le username existe déjà !");
        }

        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("L'adresse mail est déjà utilisée !");
        }

        User user = new User(username, email, password);
        return userRepository.save(user);
    }


    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }




    // etc... (modifier un user, supprimer, etc.)
}