package com.uep.wap.service;

import com.uep.wap.model.EloHistory;
import com.uep.wap.model.Player;
import com.uep.wap.repository.EloHistoryRepository;
import com.uep.wap.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EloHistoryService {

    @Autowired
    private EloHistoryRepository eloHistoryRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public Iterable<EloHistory> getAllHistory() {
        return eloHistoryRepository.findAll();
    }

    public List<EloHistory> getHistoryByPlayer(Long playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        return player.map(eloHistoryRepository::findByPlayerOrderByRecordedAtDesc).orElseGet(List::of);
    }
}