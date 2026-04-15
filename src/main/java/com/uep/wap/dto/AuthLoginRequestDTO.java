package com.uep.wap.dto;

/**
 * DTO dla żądania logowania.
 */
public class AuthLoginRequestDTO {

    private String email;
    private String password;

    public AuthLoginRequestDTO() {}

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
}
