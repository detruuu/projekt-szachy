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
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EloHistoryRepository eloHistoryRepository;

    public Iterable<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public List<EloHistory> getPlayerEloHistory(Long playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        return player.map(eloHistoryRepository::findByPlayerOrderByRecordedAtDesc).orElseGet(List::of);
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}