package com.uep.wap.dto;

/**
 * DTO dla żądania rejestracji nowego użytkownika.
 */
public class AuthRegisterRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String fideTitle;
    private String federation;

    public AuthRegisterRequestDTO() {}

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
