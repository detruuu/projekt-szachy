package com.uep.wap.dto;

/**
 * DTO dla żądania wprowadzenia wyniku partii.
 * Zawiera tylko niezbędne dane: wynik, informacja o walkowerze.
 */
public class GameResultCreateRequestDTO {

    private String result; // 1-0, 0-1, ½-½
    private Boolean forfeit;

    public GameResultCreateRequestDTO() {}

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
}
