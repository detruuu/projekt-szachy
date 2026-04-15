package com.uep.wap.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="elo_history")
public class EloHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name="tournament_id")
    private Tournament tournament;

    @Column(name="rating_before")
    private Integer ratingBefore;

    @Column(name="rating_after")
    private Integer ratingAfter;

    @Column(name="delta")
    private Integer delta;

    @Column(name="recorded_at")
    private LocalDateTime recordedAt;

    public EloHistory(){}

    // gettery/settery
}