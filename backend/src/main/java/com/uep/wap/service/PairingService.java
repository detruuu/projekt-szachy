package com.uep.wap.service;

import com.uep.wap.dto.GameResultCreateRequestDTO;
import com.uep.wap.dto.GameResultDTO;
import com.uep.wap.model.GameResult;
import com.uep.wap.model.Pairing;
import com.uep.wap.repository.GameResultRepository;
import com.uep.wap.repository.PairingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PairingService {

    @Autowired
    private PairingRepository pairingRepository;

    @Autowired
    private GameResultRepository gameResultRepository;

    public Optional<Pairing> getPairingById(Long id) {
        return pairingRepository.findById(id);
    }

    public Optional<GameResultDTO> setResult(Long pairingId, GameResultCreateRequestDTO requestDTO) {
        Optional<Pairing> pairing = pairingRepository.findById(pairingId);
        if (pairing.isEmpty()) {
            return Optional.empty();
        }

        GameResult gameResult = gameResultRepository.findByPairing(pairing.get());
        if (gameResult == null) {
            gameResult = new GameResult();
            gameResult.setPairing(pairing.get());
        }

        gameResult.setResult(requestDTO.getResult());
        gameResult.setForfeit(requestDTO.getForfeit() != null && requestDTO.getForfeit());
        gameResult.setPlayedAt(LocalDateTime.now());

        GameResult savedResult = gameResultRepository.save(gameResult);
        return Optional.of(mapToDto(savedResult));
    }

    private GameResultDTO mapToDto(GameResult gameResult) {
        GameResultDTO dto = new GameResultDTO();
        dto.setId(gameResult.getId());
        dto.setPairingId(gameResult.getPairing().getId());
        dto.setResult(gameResult.getResult());
        dto.setForfeit(gameResult.getForfeit());
        dto.setPlayedAt(gameResult.getPlayedAt());
        return dto;
    }
}