package com.uep.wap.controller;

import com.uep.wap.dto.TournamentCreateRequestDTO;
import com.uep.wap.dto.TournamentDTO;
import com.uep.wap.model.Round;
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
    public List<TournamentDTO> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TournamentDTO> getTournament(@PathVariable Long id) {
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
    public ResponseEntity<TournamentDTO> createTournament(@RequestBody TournamentCreateRequestDTO requestDTO) {
        TournamentDTO created = tournamentService.createTournament(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<TournamentDTO> updateTournament(@PathVariable Long id, @RequestBody TournamentCreateRequestDTO requestDTO) {
        return tournamentService.updateTournament(id, requestDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        if (!tournamentService.deleteTournament(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{id}/close")
    public ResponseEntity<TournamentDTO> closeTournament(@PathVariable Long id) {
        return tournamentService.closeTournament(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}