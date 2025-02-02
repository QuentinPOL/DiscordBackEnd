package com.example.discordbackend.controller;

import com.example.discordbackend.dto.FriendRequestDTO;
import com.example.discordbackend.model.Friendship;
import com.example.discordbackend.model.User;
import com.example.discordbackend.service.FriendshipService;
import com.example.discordbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<FriendRequestDTO>> getFriends() {
        List<Friendship> friendships = friendshipService.getAllAcceptedFriendships();
        List<FriendRequestDTO> response = friendships.stream()
                .map(FriendRequestDTO::fromFriendship) // ou tout autre builder
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequestDTO>> getPendingFriends() {
        List<Friendship> pendingFriendships = friendshipService.getPendingFriendships();
        List<FriendRequestDTO> response = pendingFriendships.stream()
                .map(FriendRequestDTO::fromFriendship)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }



    // ---------- Envoyer une demande d'ami ----------
    @PostMapping("/send-request")
    public ResponseEntity<FriendRequestDTO> sendFriendRequest(@RequestBody FriendRequestDTO request) {
        Friendship friendship = friendshipService.sendFriendRequest(request.getFriendUsername());
        FriendRequestDTO dto = FriendRequestDTO.fromSendRequest(friendship);
        return ResponseEntity.ok(dto);
    }

    // ---------- Accepter une demande d'ami ----------
    @PostMapping("/accept-request")
    public ResponseEntity<FriendRequestDTO> acceptFriendRequest(@RequestBody FriendRequestDTO request) {
        Friendship friendship = friendshipService.acceptFriendRequest(request.getFriendshipId());
        FriendRequestDTO dto = FriendRequestDTO.fromAcceptRequest(friendship);
        return ResponseEntity.ok(dto);
    }

    // ---------- Supprimer un ami / Annuler la relation ----------
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFriend(@RequestBody FriendRequestDTO request) {
        friendshipService.removeFriend(request.getFriendUsername());
        return ResponseEntity.noContent().build();
    }
}
