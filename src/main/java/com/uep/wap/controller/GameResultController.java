package com.uep.wap.controller;

import com.uep.wap.model.GameResult;
import com.uep.wap.service.GameResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/game-results")
public class GameResultController {

    private final GameResultService gameResultService;

    public GameResultController(GameResultService gameResultService) {
        this.gameResultService = gameResultService;
    }

    @GetMapping
    public Iterable<GameResult> getAllResults() {
        return gameResultService.getAllResults();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GameResult> getResult(@PathVariable Long id) {
        return gameResultService.getResultById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}