package com.uep.wap.dto;

public class PlayerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer eloRating;
    private String fideTitle;
    private String federation;

    public PlayerDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEloRating() {
        return eloRating;
    }

    public void setEloRating(Integer eloRating) {
        this.eloRating = eloRating;
    }

    public String getFideTitle() {
        return fideTitle;
    }

    public void setFideTitle(String fideTitle) {
        this.fideTitle = fideTitle;
    }

    public String getFederation() {
        return federation;
    }

    public void setFederation(String federation) {
        this.federation = federation;
    }
}
