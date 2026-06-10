package com.uep.wap.service;

import com.uep.wap.model.Pairing;
import com.uep.wap.model.Player;
import com.uep.wap.model.Round;
import com.uep.wap.model.Tournament;
import com.uep.wap.repository.PairingRepository;
import com.uep.wap.repository.PlayerRepository;
import com.uep.wap.repository.RoundRepository;
import com.uep.wap.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class RoundService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private PairingRepository pairingRepository;

    @Autowired
    private PlayerRepository playerRepository;

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

    public Optional<Round> generateNextRound(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) {
            return Optional.empty();
        }

        List<Round> existingRounds = roundRepository.findByTournament(tournament.get());
        int nextRoundNumber = existingRounds.size() + 1;

        Round round = new Round();
        round.setTournament(tournament.get());
        round.setRoundNumber(nextRoundNumber);
        round.setStatus("GENERATED");
        round.setGeneratedAt(LocalDateTime.now());
        Round savedRound = roundRepository.save(round);

        List<Player> players = StreamSupport.stream(playerRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Player::getEloRating, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        int board = 1;
        for (int i = 0; i + 1 < players.size(); i += 2) {
            Pairing pairing = new Pairing();
            pairing.setRound(savedRound);
            pairing.setPlayerWhite(players.get(i));
            pairing.setPlayerBlack(players.get(i + 1));
            pairing.setBoardNumber(board++);
            pairingRepository.save(pairing);
        }

        return Optional.of(savedRound);
    }
}