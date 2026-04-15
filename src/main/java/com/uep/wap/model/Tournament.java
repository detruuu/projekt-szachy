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

    // gettery/settery
}