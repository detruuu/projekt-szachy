package com.uep.wap.controller;

import com.uep.wap.model.Pairing;
import com.uep.wap.model.Round;
import com.uep.wap.service.RoundService;
import com.uep.wap.service.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/tournaments/{tournamentId}/rounds")
public class RoundController {

    private final TournamentService tournamentService;
    private final RoundService roundService;

    public RoundController(TournamentService tournamentService, RoundService roundService) {
        this.tournamentService = tournamentService;
        this.roundService = roundService;
    }

    @GetMapping
    public ResponseEntity<List<Round>> getRounds(@PathVariable Long tournamentId) {
        return tournamentService.getTournamentById(tournamentId)
            .map(tournament -> ResponseEntity.ok(roundService.getRoundsForTournament(tournamentId)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{roundNumber}")
    public ResponseEntity<List<Pairing>> getPairingsForRound(@PathVariable Long tournamentId, @PathVariable Integer roundNumber) {
        return tournamentService.getTournamentById(tournamentId)
                .map(tournament -> ResponseEntity.ok(roundService.getPairingsForRound(tournamentId, roundNumber)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/generate")
    public ResponseEntity<String> generateNextRound(@PathVariable Long tournamentId) {
        if (tournamentService.getTournamentById(tournamentId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament not found: " + tournamentId);
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("Round generation will be added later for tournamentId=" + tournamentId);
    }
}