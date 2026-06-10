package com.uep.wap.controller;

import com.uep.wap.dto.PlayerDTO;
import com.uep.wap.model.EloHistory;
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
    public List<PlayerDTO> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable Long id) {
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
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO playerDTO) {
        PlayerDTO created = playerService.createPlayer(playerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long id, @RequestBody PlayerDTO playerDTO) {
        return playerService.updatePlayer(id, playerDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        if (!playerService.deletePlayer(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}