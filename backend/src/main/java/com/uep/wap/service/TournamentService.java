package com.uep.wap.service;

import com.uep.wap.dto.TournamentCreateRequestDTO;
import com.uep.wap.dto.TournamentDTO;
import com.uep.wap.model.Round;
import com.uep.wap.model.Tournament;
import com.uep.wap.repository.RoundRepository;
import com.uep.wap.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private EloCalculationService eloCalculationService;

    public List<TournamentDTO> getAllTournaments() {
        return StreamSupport.stream(tournamentRepository.findAll().spliterator(), false)
                .map(this::mapToDto)
                .toList();
    }

    public Optional<TournamentDTO> getTournamentById(Long id) {
        return tournamentRepository.findById(id).map(this::mapToDto);
    }

    public List<Round> getRoundsForTournament(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        return tournament.map(roundRepository::findByTournament).orElseGet(List::of);
    }

    public TournamentDTO createTournament(TournamentCreateRequestDTO requestDTO) {
        Tournament tournament = new Tournament();
        applyRequestToEntity(requestDTO, tournament);
        if (tournament.getStatus() == null) {
            tournament.setStatus("PLANNED");
        }
        return mapToDto(tournamentRepository.save(tournament));
    }

    public Optional<TournamentDTO> updateTournament(Long id, TournamentCreateRequestDTO requestDTO) {
        Optional<Tournament> existing = tournamentRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }

        Tournament tournament = existing.get();
        applyRequestToEntity(requestDTO, tournament);
        return Optional.of(mapToDto(tournamentRepository.save(tournament)));
    }

    public boolean deleteTournament(Long id) {
        if (!tournamentRepository.existsById(id)) {
            return false;
        }
        tournamentRepository.deleteById(id);
        return true;
    }

    public Optional<TournamentDTO> closeTournament(Long tournamentId) {
        Optional<Tournament> existing = tournamentRepository.findById(tournamentId);
        if (existing.isEmpty()) {
            return Optional.empty();
        }

        Tournament tournament = existing.get();
        if (!"CLOSED".equalsIgnoreCase(tournament.getStatus())) {
            eloCalculationService.recalculateTournamentElo(tournamentId);
        }
        tournament.setStatus("CLOSED");
        return Optional.of(mapToDto(tournamentRepository.save(tournament)));
    }

    private TournamentDTO mapToDto(Tournament tournament) {
        TournamentDTO dto = new TournamentDTO();
        dto.setId(tournament.getId());
        dto.setName(tournament.getName());
        dto.setFormat(tournament.getFormat());
        dto.setStatus(tournament.getStatus());
        dto.setRoundsTotal(tournament.getRoundsTotal());
        dto.setTimeControl(tournament.getTimeControl());
        dto.setKFactor(tournament.getKFactor());
        dto.setStartDate(tournament.getStartDate());
        dto.setCreatedBy(tournament.getCreatedBy());
        return dto;
    }

    private void applyRequestToEntity(TournamentCreateRequestDTO requestDTO, Tournament tournament) {
        tournament.setName(requestDTO.getName());
        tournament.setFormat(requestDTO.getFormat());
        tournament.setRoundsTotal(requestDTO.getRoundsTotal());
        tournament.setTimeControl(requestDTO.getTimeControl());
        tournament.setKFactor(requestDTO.getKFactor());
        tournament.setStartDate(requestDTO.getStartDate());
    }
}