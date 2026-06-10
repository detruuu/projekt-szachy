package com.uep.wap.service;

import com.uep.wap.model.EloHistory;
import com.uep.wap.model.GameResult;
import com.uep.wap.model.Pairing;
import com.uep.wap.model.Player;
import com.uep.wap.model.Round;
import com.uep.wap.model.Tournament;
import com.uep.wap.repository.EloHistoryRepository;
import com.uep.wap.repository.GameResultRepository;
import com.uep.wap.repository.PairingRepository;
import com.uep.wap.repository.PlayerRepository;
import com.uep.wap.repository.RoundRepository;
import com.uep.wap.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EloCalculationService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private PairingRepository pairingRepository;

    @Autowired
    private GameResultRepository gameResultRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EloHistoryRepository eloHistoryRepository;

    public double calculateExpectedScore(int ratingA, int ratingB) {
        return 1.0 / (1.0 + Math.pow(10.0, (ratingB - ratingA) / 400.0));
    }

    public int calculateRatingChange(int ratingA, int ratingB, double score, int kFactor) {
        double expected = calculateExpectedScore(ratingA, ratingB);
        return (int) Math.round(kFactor * (score - expected));
    }

    public void recalculateTournamentElo(Long tournamentId) {
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        if (tournamentOptional.isEmpty()) {
            return;
        }

        Tournament tournament = tournamentOptional.get();
        if (!eloHistoryRepository.findByTournament(tournament).isEmpty()) {
            return;
        }

        List<Round> rounds = roundRepository.findByTournament(tournament);
        Map<Long, Integer> ratingBefore = new HashMap<>();
        Map<Long, Double> deltaByPlayer = new HashMap<>();
        Map<Long, Player> playersById = new HashMap<>();

        for (Round round : rounds) {
            List<Pairing> pairings = pairingRepository.findByRound(round);
            for (Pairing pairing : pairings) {
                GameResult gameResult = gameResultRepository.findByPairing(pairing);
                if (gameResult == null || gameResult.getResult() == null) {
                    continue;
                }

                Player white = pairing.getPlayerWhite();
                Player black = pairing.getPlayerBlack();
                if (white == null || black == null) {
                    continue;
                }

                playersById.put(white.getId(), white);
                playersById.put(black.getId(), black);
                ratingBefore.putIfAbsent(white.getId(), white.getEloRating() == null ? 1000 : white.getEloRating());
                ratingBefore.putIfAbsent(black.getId(), black.getEloRating() == null ? 1000 : black.getEloRating());

                double[] scores = parseScore(gameResult.getResult());
                if (scores == null) {
                    continue;
                }

                int whiteRating = ratingBefore.get(white.getId());
                int blackRating = ratingBefore.get(black.getId());
                double expectedWhite = calculateExpectedScore(whiteRating, blackRating);
                double expectedBlack = 1.0 - expectedWhite;

                int kWhite = resolveKFactor(tournament.getKFactor(), whiteRating);
                int kBlack = resolveKFactor(tournament.getKFactor(), blackRating);

                double whiteDelta = kWhite * (scores[0] - expectedWhite);
                double blackDelta = kBlack * (scores[1] - expectedBlack);

                deltaByPlayer.put(white.getId(), deltaByPlayer.getOrDefault(white.getId(), 0.0) + whiteDelta);
                deltaByPlayer.put(black.getId(), deltaByPlayer.getOrDefault(black.getId(), 0.0) + blackDelta);
            }
        }

        for (Map.Entry<Long, Double> entry : deltaByPlayer.entrySet()) {
            Long playerId = entry.getKey();
            Player player = playersById.get(playerId);
            if (player == null) {
                continue;
            }

            int before = ratingBefore.getOrDefault(playerId, player.getEloRating() == null ? 1000 : player.getEloRating());
            int delta = (int) Math.round(entry.getValue());
            int after = before + delta;

            player.setEloRating(after);
            playerRepository.save(player);

            EloHistory history = new EloHistory();
            history.setPlayer(player);
            history.setTournament(tournament);
            history.setRatingBefore(before);
            history.setRatingAfter(after);
            history.setDelta(delta);
            history.setRecordedAt(LocalDateTime.now());
            eloHistoryRepository.save(history);
        }
    }

    private int resolveKFactor(Integer tournamentK, int rating) {
        if (tournamentK != null && tournamentK > 0) {
            return tournamentK;
        }
        return rating >= 2400 ? 10 : 20;
    }

    private double[] parseScore(String result) {
        String normalized = result.trim();
        if ("1-0".equals(normalized)) {
            return new double[]{1.0, 0.0};
        }
        if ("0-1".equals(normalized)) {
            return new double[]{0.0, 1.0};
        }
        if ("1/2-1/2".equals(normalized) || "0.5-0.5".equals(normalized) || "½-½".equals(normalized)) {
            return new double[]{0.5, 0.5};
        }
        return null;
    }
}