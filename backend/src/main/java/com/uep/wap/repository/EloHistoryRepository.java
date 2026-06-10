package com.uep.wap.repository;

import com.uep.wap.model.EloHistory;
import com.uep.wap.model.Player;
import com.uep.wap.model.Tournament;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EloHistoryRepository extends CrudRepository<EloHistory, Long> {

    List<EloHistory> findByPlayer(Player player);

    List<EloHistory> findByTournament(Tournament tournament);

    List<EloHistory> findByPlayerOrderByRecordedAtDesc(Player player);
}