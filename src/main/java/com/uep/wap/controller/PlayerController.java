package com.uep.wap.controller;

import com.uep.wap.model.EloHistory;
import com.uep.wap.model.Player;
import com.uep.wap.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public Iterable<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        return playerService.getPlayerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{id}/elo-history")
    public ResponseEntity<List<EloHistory>> getPlayerEloHistory(@PathVariable Long id) {
        return playerService.getPlayerById(id)
            .map(player -> ResponseEntity.ok(playerService.getPlayerEloHistory(id)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{id}/stats")
    public ResponseEntity<String> getPlayerStats(@PathVariable Long id) {
        return playerService.getPlayerById(id)
                .map(player -> ResponseEntity.ok("Player stats endpoint prepared for playerId=" + id))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createPlayer() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Player creation will be added later.");
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<String> updatePlayer(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Player update will be added later for id=" + id);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}