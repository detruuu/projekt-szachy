package com.uep.wap.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name")
    private String name;

    @Column(name="format")
    private String format;

    @Column(name="status")
    private String status;

    @Column(name="rounds_total")
    private Integer roundsTotal;

    @Column(name="time_control")
    private String timeControl;

    @Column(name="k_factor")
    private Integer kFactor;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="created_by")
    private Long createdBy;

    public Tournament(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}