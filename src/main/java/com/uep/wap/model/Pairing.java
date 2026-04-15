package com.uep.wap.model;

import javax.persistence.*;

@Entity
@Table(name="pairings")
public class Pairing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="round_id")
    private Round round;

    @ManyToOne
    @JoinColumn(name="player_white")
    private Player playerWhite;

    @ManyToOne
    @JoinColumn(name="player_black")
    private Player playerBlack;

    @Column(name="board_number")
    private Integer boardNumber;

    public Pairing(){}

    // gettery/settery
}