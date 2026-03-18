package com.uep.wap.model;

import com.uep.wap.model.enums.TournamentFormat;
import com.uep.wap.model.enums.TournamentStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="tournaments")
public class Tournament {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private TournamentFormat format;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TournamentStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "max_players")
    private int maxPlayers;

    @Column(name = "created_by_id")
    private Long createdById;

    public Tournament() {
    }

    public Tournament(String name, TournamentFormat format, TournamentStatus status, LocalDate startDate, LocalDate endDate, int maxPlayers, Long createdById) {
        this.name = name;
        this.format = format;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxPlayers = maxPlayers;
        this.createdById = createdById;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TournamentFormat getFormat() {
        return format;
    }

    public void setFormat(TournamentFormat format) {
        this.format = format;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }
}
