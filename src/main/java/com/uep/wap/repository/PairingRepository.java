package com.uep.wap.repository;

import com.uep.wap.model.Pairing;
import com.uep.wap.model.Round;
import com.uep.wap.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PairingRepository extends CrudRepository<Pairing, Long> {

    List<Pairing> findByRound(Round round);

    List<Pairing> findByPlayerWhiteOrPlayerBlack(Player white, Player black);
}