package com.example.discordbackend.service;

import com.example.discordbackend.model.Friendship;
import com.example.discordbackend.model.User;
import com.example.discordbackend.repository.FriendshipRepository;
import com.example.discordbackend.repository.UserRepository;
import com.example.discordbackend.security.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;


    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }


    public List<Friendship> getAllAcceptedFriendships() {
        String userEmail = CurrentUserUtils.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return friendshipRepository.findByUserIdAndStatusOrFriendIdAndStatus(
                currentUser.getId(), "ACCEPTED",
                currentUser.getId(), "ACCEPTED"
        );
    }

    public List<Friendship> getPendingFriendships() {
        String userEmail = CurrentUserUtils.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return friendshipRepository.findByUserIdAndStatusOrFriendIdAndStatus(
                currentUser.getId(), "PENDING",
                currentUser.getId(), "PENDING"
        );
    }


    // -----------------------------------------------------
    // ENVOYER UNE DEMANDE D'AMI (status = "PENDING")
    // -----------------------------------------------------
    public Friendship sendFriendRequest(String friendUsername) {
        // Récupérer l'utilisateur connecté
        String userEmail = CurrentUserUtils.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (currentUser.getUsername().equals(friendUsername)) {
            throw new RuntimeException("Vous ne pouvez pas vous ajouter vous-même en ami.");
        }

        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new RuntimeException("Ami non trouvé"));

        // Vérifier qu'aucune relation n'existe déjà dans un sens ou dans l'autre
        Friendship existing1 = friendshipRepository.findByUserIdAndFriendId(currentUser.getId(), friend.getId());
        Friendship existing2 = friendshipRepository.findByUserIdAndFriendId(friend.getId(), currentUser.getId());

        if (existing1 != null || existing2 != null) {
            throw new RuntimeException("Une relation entre ces deux utilisateurs existe déjà !");
        }

        // Créer et sauvegarder l'amitié (PENDING)
        Friendship friendship = new Friendship();
        friendship.setUser(currentUser);
        friendship.setFriend(friend);
        friendship.setStatus("PENDING");
        friendship.setCreatedAt(LocalDateTime.now());
        // Optionnel : setUpdatedAt(LocalDateTime.now());

        return friendshipRepository.save(friendship);
    }

    // -----------------------------------------------------
    // ACCEPTER UNE DEMANDE D'AMI
    // -----------------------------------------------------
    public Friendship acceptFriendRequest(Integer friendshipId) {
        // Récupérer l'utilisateur connecté
        String userEmail = CurrentUserUtils.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Retrouver la demande
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("Relation non trouvée"));

        // Vérifier que c'est bien la "friend" (le destinataire) qui accepte
        if (!friendship.getFriend().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à accepter cette demande.");
        }

        // Mettre à jour le statut
        friendship.setStatus("ACCEPTED");
        friendship.setUpdatedAt(LocalDateTime.now());

        return friendshipRepository.save(friendship);
    }

    // -----------------------------------------------------
    // SUPPRIMER / RETIRER UN AMI
    // -----------------------------------------------------
    public void removeFriend(String friendUsername) {
        // Récupérer l'utilisateur connecté
        String userEmail = CurrentUserUtils.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new RuntimeException("Ami non trouvé"));

        // Chercher la relation dans un sens, puis dans l'autre
        Friendship friendship = friendshipRepository.findByUserIdAndFriendId(currentUser.getId(), friend.getId());
        if (friendship == null) {
            friendship = friendshipRepository.findByUserIdAndFriendId(friend.getId(), currentUser.getId());
        }

        if (friendship == null) {
            throw new RuntimeException("Relation non trouvée !");
        }

        // Supprimer la relation
        friendshipRepository.delete(friendship);
    }
}
