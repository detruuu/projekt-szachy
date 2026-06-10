package com.uep.wap.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StandingsService {

    public List<Map<String, Object>> getStandings(Long tournamentId) {
        throw new UnsupportedOperationException("Standings and tie-break calculations are not implemented yet.");
    }

    public List<Map<String, Object>> getCrosstable(Long tournamentId) {
        throw new UnsupportedOperationException("Crosstable calculation is not implemented yet.");
    }
}