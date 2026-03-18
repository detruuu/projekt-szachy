package com.uep.wap.model;

import javax.persistence.*;

@Entity
@Table(name="set_scores")
public class SetScore {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(name = "set_number")
    private int setNumber;

    @Column(name = "games_player1")
    private int gamesPlayer1;

    @Column(name = "games_player2")
    private int gamesPlayer2;

    @Column(name = "tiebreak")
    private boolean tiebreak;

    @Column(name = "tiebreak_player1")
    private int tiebreakPlayer1;

    @Column(name = "tiebreak_player2")
    private int tiebreakPlayer2;

    public SetScore() {
    }

    public SetScore(Match match, int setNumber, int gamesPlayer1, int gamesPlayer2, boolean tiebreak, int tiebreakPlayer1, int tiebreakPlayer2) {
        this.match = match;
        this.setNumber = setNumber;
        this.gamesPlayer1 = gamesPlayer1;
        this.gamesPlayer2 = gamesPlayer2;
        this.tiebreak = tiebreak;
        this.tiebreakPlayer1 = tiebreakPlayer1;
        this.tiebreakPlayer2 = tiebreakPlayer2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public int getGamesPlayer1() {
        return gamesPlayer1;
    }

    public void setGamesPlayer1(int gamesPlayer1) {
        this.gamesPlayer1 = gamesPlayer1;
    }

    public int getGamesPlayer2() {
        return gamesPlayer2;
    }

    public void setGamesPlayer2(int gamesPlayer2) {
        this.gamesPlayer2 = gamesPlayer2;
    }

    public boolean isTiebreak() {
        return tiebreak;
    }

    public void setTiebreak(boolean tiebreak) {
        this.tiebreak = tiebreak;
    }

    public int getTiebreakPlayer1() {
        return tiebreakPlayer1;
    }

    public void setTiebreakPlayer1(int tiebreakPlayer1) {
        this.tiebreakPlayer1 = tiebreakPlayer1;
    }

    public int getTiebreakPlayer2() {
        return tiebreakPlayer2;
    }

    public void setTiebreakPlayer2(int tiebreakPlayer2) {
        this.tiebreakPlayer2 = tiebreakPlayer2;
    }
}
