package com.uep.wap.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="game_results")
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name="pairing_id")
    private Pairing pairing;

    @Column(name="result")
    private String result;

    @Column(name="forfeit")
    private Boolean forfeit;

    @Column(name="played_at")
    private LocalDateTime playedAt;

    public GameResult(){}

    // gettery/settery
}