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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public Player getPlayerWhite() {
        return playerWhite;
    }

    public void setPlayerWhite(Player playerWhite) {
        this.playerWhite = playerWhite;
    }

    public Player getPlayerBlack() {
        return playerBlack;
    }

    public void setPlayerBlack(Player playerBlack) {
        this.playerBlack = playerBlack;
    }

    public Integer getBoardNumber() {
        return boardNumber;
    }

    public void setBoardNumber(Integer boardNumber) {
        this.boardNumber = boardNumber;
    }
}