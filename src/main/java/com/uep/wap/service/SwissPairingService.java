package com.uep.wap.service;

import com.uep.wap.model.Pairing;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwissPairingService {

    public List<Pairing> generatePairingsForNextRound(Long tournamentId) {
        throw new UnsupportedOperationException("Swiss pairing algorithm (FIDE) is not implemented yet.");
    }
}