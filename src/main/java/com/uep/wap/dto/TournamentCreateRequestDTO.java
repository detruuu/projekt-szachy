package com.uep.wap.dto;

import java.time.LocalDate;

/**
 * DTO dla żądania tworzenia turnieju.
 * Nie zawiera id i createdBy (generowane przez system).
 */
public class TournamentCreateRequestDTO {

    private String name;
    private String format;
    private Integer roundsTotal;
    private String timeControl;
    private Integer kFactor;
    private LocalDate startDate;

    public TournamentCreateRequestDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getRoundsTotal() {
        return roundsTotal;
    }

    public void setRoundsTotal(Integer roundsTotal) {
        this.roundsTotal = roundsTotal;
    }

    public String getTimeControl() {
        return timeControl;
    }

    public void setTimeControl(String timeControl) {
        this.timeControl = timeControl;
    }

    public Integer getKFactor() {
        return kFactor;
    }

    public void setKFactor(Integer kFactor) {
        this.kFactor = kFactor;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
