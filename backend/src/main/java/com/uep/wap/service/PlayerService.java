package com.uep.wap.service;

import com.uep.wap.dto.PlayerDTO;
import com.uep.wap.model.EloHistory;
import com.uep.wap.model.Player;
import com.uep.wap.repository.EloHistoryRepository;
import com.uep.wap.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private EloHistoryRepository eloHistoryRepository;

    public List<PlayerDTO> getAllPlayers() {
        return StreamSupport.stream(playerRepository.findAll().spliterator(), false)
                .map(this::mapToDto)
                .toList();
    }

    public Optional<PlayerDTO> getPlayerById(Long id) {
        return playerRepository.findById(id).map(this::mapToDto);
    }

    public List<EloHistory> getPlayerEloHistory(Long playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        return player.map(eloHistoryRepository::findByPlayerOrderByRecordedAtDesc).orElseGet(List::of);
    }

    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        Player player = new Player();
        applyDtoToEntity(playerDTO, player);
        if (player.getEloRating() == null) {
            player.setEloRating(1000);
        }
        player.setCreatedAt(LocalDateTime.now());
        return mapToDto(playerRepository.save(player));
    }

    public Optional<PlayerDTO> updatePlayer(Long id, PlayerDTO playerDTO) {
        Optional<Player> existing = playerRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }

        Player player = existing.get();
        applyDtoToEntity(playerDTO, player);
        return Optional.of(mapToDto(playerRepository.save(player)));
    }

    public boolean deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            return false;
        }
        playerRepository.deleteById(id);
        return true;
    }

    private PlayerDTO mapToDto(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setFirstName(player.getFirstName());
        dto.setLastName(player.getLastName());
        dto.setEmail(player.getEmail());
        dto.setEloRating(player.getEloRating());
        dto.setFideTitle(player.getFideTitle());
        dto.setFederation(player.getFederation());
        return dto;
    }

    private void applyDtoToEntity(PlayerDTO dto, Player player) {
        player.setFirstName(dto.getFirstName());
        player.setLastName(dto.getLastName());
        player.setEmail(dto.getEmail());
        player.setEloRating(dto.getEloRating());
        player.setFideTitle(dto.getFideTitle());
        player.setFederation(dto.getFederation());
    }
}