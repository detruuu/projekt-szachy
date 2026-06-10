package com.uep.wap.repository;

import com.uep.wap.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    Player findByEmail(String email);
}