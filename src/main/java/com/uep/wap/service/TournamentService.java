package com.uep.wap.service;

import com.uep.wap.model.Round;
import com.uep.wap.model.Tournament;
import com.uep.wap.repository.RoundRepository;
import com.uep.wap.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private RoundRepository roundRepository;

    public Iterable<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Optional<Tournament> getTournamentById(Long id) {
        return tournamentRepository.findById(id);
    }

    public List<Round> getRoundsForTournament(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        return tournament.map(roundRepository::findByTournament).orElseGet(List::of);
    }

    public Tournament saveTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }

    public Optional<Tournament> closeTournament(Long tournamentId) {
        // TODO: Ustawić status CLOSED po dodaniu pełnej logiki i setterów w modelu.
        return tournamentRepository.findById(tournamentId);
    }
}