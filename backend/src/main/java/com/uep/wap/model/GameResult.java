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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Pairing getPairing() {
        return pairing;
    }

    public void setPairing(Pairing pairing) {
        this.pairing = pairing;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Boolean getForfeit() {
        return forfeit;
    }

    public void setForfeit(Boolean forfeit) {
        this.forfeit = forfeit;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }
}