package com.uep.wap.service;

import com.uep.wap.model.GameResult;
import com.uep.wap.repository.GameResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameResultService {

    @Autowired
    private GameResultRepository gameResultRepository;

    public Iterable<GameResult> getAllResults() {
        return gameResultRepository.findAll();
    }

    public Optional<GameResult> getResultById(Long id) {
        return gameResultRepository.findById(id);
    }
}