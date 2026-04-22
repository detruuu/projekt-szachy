package com.uep.wap.service;

import com.uep.wap.dto.GameResultCreateRequestDTO;
import com.uep.wap.model.Pairing;
import com.uep.wap.repository.GameResultRepository;
import com.uep.wap.repository.PairingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void setResult(Long pairingId, GameResultCreateRequestDTO requestDTO) {
        throw new UnsupportedOperationException("Result registration logic will be added in GameResultService.");
    }
}