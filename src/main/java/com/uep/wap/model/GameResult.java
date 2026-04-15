package com.uep.wap.model;

import javax.persistence.*;

@Entity
@Table(name="students")
public class Tournament {
    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name ="name")
    private String name;
    @Column(name ="points")
    private Integer points;


    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return id;
    }
    public Tournament(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Tournament(String name, Integer points){

    }
}

