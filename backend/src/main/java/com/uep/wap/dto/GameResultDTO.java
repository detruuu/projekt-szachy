package com.uep.wap.dto;

import java.time.LocalDateTime;

public class GameResultDTO {

    private Long id;
    private Long pairingId;
    private String result;
    private Boolean forfeit;
    private LocalDateTime playedAt;

    public GameResultDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPairingId() {
        return pairingId;
    }

    public void setPairingId(Long pairingId) {
        this.pairingId = pairingId;
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
