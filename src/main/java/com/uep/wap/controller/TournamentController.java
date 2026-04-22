package com.uep.wap.controller;

import com.uep.wap.model.Round;
import com.uep.wap.model.Tournament;
import com.uep.wap.service.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping
    public Iterable<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Tournament> getTournament(@PathVariable Long id) {
        return tournamentService.getTournamentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{id}/rounds")
    public ResponseEntity<List<Round>> getTournamentRounds(@PathVariable Long id) {
        return tournamentService.getTournamentById(id)
            .map(tournament -> ResponseEntity.ok(tournamentService.getRoundsForTournament(id)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createTournament() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Tournament creation will be added later.");
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<String> updateTournament(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Tournament update will be added later for id=" + id);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping(path = "/{id}/close")
    public ResponseEntity<String> closeTournament(@PathVariable Long id) {
        return tournamentService.closeTournament(id)
                .map(tournament -> ResponseEntity.ok("Tournament close endpoint prepared for id=" + id))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}