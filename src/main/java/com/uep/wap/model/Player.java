package com.uep.wap.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="elo_rating")
    private Integer eloRating;

    @Column(name="fide_title")
    private String fideTitle;

    @Column(name="federation")
    private String federation;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    public Player(){}

    // gettery/settery
}