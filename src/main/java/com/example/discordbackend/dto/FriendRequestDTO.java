package com.example.discordbackend.dto;

import com.example.discordbackend.model.Friendship;
import com.example.discordbackend.model.User;

public class FriendRequestDTO {
    private String friendUsername;
    private Integer friendshipId;
    private String status;
    private String avatar; // ✅ On veut aussi renvoyer l’avatar de l’ami

    // 🔹 Constructeur par défaut (nécessaire pour la désérialisation JSON)
    public FriendRequestDTO() {}

    // 🔹 Constructeur complet
    public FriendRequestDTO(String friendUsername, Integer friendshipId, String status, String avatar) {
        this.friendUsername = friendUsername;
        this.friendshipId = friendshipId;
        this.status = status;
        this.avatar = avatar;
    }

    // ---------- Getters & Setters ----------
    public String getFriendUsername() {
        return friendUsername;
    }
    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public Integer getFriendshipId() {
        return friendshipId;
    }
    public void setFriendshipId(Integer friendshipId) {
        this.friendshipId = friendshipId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // ---------- Méthodes statiques de fabrication du DTO ----------

    /**
     * Convertit un Friendship en FriendRequestDTO générique
     * (par exemple pour lister les amis ou afficher l’état d’une amitié).
     */
    public static FriendRequestDTO fromFriendship(Friendship friendship) {
        User friendUser = friendship.getFriend(); // L’ami en question
        return new FriendRequestDTO(
                friendUser.getUsername(),
                friendship.getId(),
                friendship.getStatus(),
                friendUser.getAvatar()
        );
    }

    /**
     * Lors de l’envoi d’une demande, on renvoie un DTO similaire,
     * mais on peut si besoin personnaliser certains champs.
     */
    public static FriendRequestDTO fromSendRequest(Friendship friendship) {
        // Ici, on peut réutiliser la logique de fromFriendship :
        return fromFriendship(friendship);
        // Ou faire un truc plus spécifique si tu veux, ex:
        // return new FriendRequestDTO(
        //     friendship.getFriend().getUsername(),
        //     friendship.getId(),
        //     "PENDING", // imposer un statut
        //     friendship.getFriend().getAvatar()
        // );
    }

    /**
     * Lors de l’acceptation d’une demande,
     * on peut également personnaliser si nécessaire.
     */
    public static FriendRequestDTO fromAcceptRequest(Friendship friendship) {
        return fromFriendship(friendship);
    }
}
