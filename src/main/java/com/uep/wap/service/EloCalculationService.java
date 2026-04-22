package com.uep.wap.service;

import org.springframework.stereotype.Service;

@Service
public class EloCalculationService {

    public double calculateExpectedScore(int ratingA, int ratingB) {
        return 1.0 / (1.0 + Math.pow(10.0, (ratingB - ratingA) / 400.0));
    }

    public int calculateRatingChange(int ratingA, int ratingB, double score, int kFactor) {
        double expected = calculateExpectedScore(ratingA, ratingB);
        return (int) Math.round(kFactor * (score - expected));
    }

    public void recalculateTournamentElo(Long tournamentId) {
        throw new UnsupportedOperationException("Tournament ELO recalculation flow is not implemented yet.");
    }
}