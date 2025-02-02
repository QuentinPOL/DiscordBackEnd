package com.example.discordbackend.repository;

import com.example.discordbackend.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    // Trouver les relations pour un utilisateur donné
    List<Friendship> findByUserId(Integer userId);

    // Trouver les relations acceptées pour un utilisateur donné
    List<Friendship> findByUserIdAndStatus(Integer userId, String status);

    // Trouver si une relation existe entre deux utilisateurs
    Friendship findByUserIdAndFriendId(Integer userId, Integer friendId);

    List<Friendship> findByUserIdAndStatusOrFriendIdAndStatus(Integer userId, String status1, Integer friendId, String status2);

}
