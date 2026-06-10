package com.uep.wap.repository;

import com.uep.wap.model.Round;
import com.uep.wap.model.Tournament;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundRepository extends CrudRepository<Round, Long> {

    List<Round> findByTournament(Tournament tournament);

    Round findByTournamentAndRoundNumber(Tournament tournament, Integer roundNumber);
}