package com.uep.wap.controller;

import com.uep.wap.dto.GameResultCreateRequestDTO;
import com.uep.wap.model.Pairing;
import com.uep.wap.service.PairingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/pairings")
public class PairingController {

    private final PairingService pairingService;

    public PairingController(PairingService pairingService) {
        this.pairingService = pairingService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Pairing> getPairing(@PathVariable Long id) {
        return pairingService.getPairingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{id}/result")
    public ResponseEntity<String> setResult(@PathVariable Long id, @RequestBody GameResultCreateRequestDTO requestDTO) {
        if (pairingService.getPairingById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pairing not found: " + id);
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("Result entry will be added later for pairingId=" + id);
    }
}