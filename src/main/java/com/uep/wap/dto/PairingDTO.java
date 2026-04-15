package com.uep.wap.dto;

public class PairingDTO {

    private Long id;
    private Long roundId;
    private Long playerWhiteId;
    private Long playerBlackId;
    private Integer boardNumber;

    public PairingDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Long getPlayerWhiteId() {
        return playerWhiteId;
    }

    public void setPlayerWhiteId(Long playerWhiteId) {
        this.playerWhiteId = playerWhiteId;
    }

    public Long getPlayerBlackId() {
        return playerBlackId;
    }

    public void setPlayerBlackId(Long playerBlackId) {
        this.playerBlackId = playerBlackId;
    }

    public Integer getBoardNumber() {
        return boardNumber;
    }

    public void setBoardNumber(Integer boardNumber) {
        this.boardNumber = boardNumber;
    }
}
