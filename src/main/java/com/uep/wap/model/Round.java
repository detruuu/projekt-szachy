package com.uep.wap.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="rounds")
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="tournament_id")
    private Tournament tournament;

    @Column(name="round_number")
    private Integer roundNumber;

    @Column(name="status")
    private String status;

    @Column(name="generated_at")
    private LocalDateTime generatedAt;

    public Round(){}

    // gettery/settery
}