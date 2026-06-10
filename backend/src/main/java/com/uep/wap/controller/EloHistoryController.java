package com.uep.wap.controller;

import com.uep.wap.model.EloHistory;
import com.uep.wap.service.EloHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/elo-history")
public class EloHistoryController {

    private final EloHistoryService eloHistoryService;

    public EloHistoryController(EloHistoryService eloHistoryService) {
        this.eloHistoryService = eloHistoryService;
    }

    @GetMapping
    public Iterable<EloHistory> getAll() {
        return eloHistoryService.getAllHistory();
    }

    @GetMapping(path = "/player/{playerId}")
    public ResponseEntity<?> getByPlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(eloHistoryService.getHistoryByPlayer(playerId));
    }
}