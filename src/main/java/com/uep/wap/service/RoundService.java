package com.uep.wap.service;

import com.uep.wap.model.Pairing;
import com.uep.wap.model.Round;
import com.uep.wap.model.Tournament;
import com.uep.wap.repository.PairingRepository;
import com.uep.wap.repository.RoundRepository;
import com.uep.wap.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoundService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private PairingRepository pairingRepository;

    public List<Round> getRoundsForTournament(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        return tournament.map(roundRepository::findByTournament).orElseGet(List::of);
    }

    public List<Pairing> getPairingsForRound(Long tournamentId, Integer roundNumber) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) {
            return List.of();
        }

        Round round = roundRepository.findByTournamentAndRoundNumber(tournament.get(), roundNumber);
        return round == null ? List.of() : pairingRepository.findByRound(round);
    }

    public void generateNextRound(Long tournamentId) {
        throw new UnsupportedOperationException("Round generation logic will be added in SwissPairingService.");
    }
}