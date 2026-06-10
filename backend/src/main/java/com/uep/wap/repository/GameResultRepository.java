package com.uep.wap.repository;

import com.uep.wap.model.GameResult;
import com.uep.wap.model.Pairing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameResultRepository extends CrudRepository<GameResult, Long> {

    GameResult findByPairing(Pairing pairing);
}