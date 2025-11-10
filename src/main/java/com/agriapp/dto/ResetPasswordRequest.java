package com.agriapp.dto;


public class ResetPasswordRequest {
    private String email;

    // Constructeurs
    public ResetPasswordRequest() {}

    public ResetPasswordRequest(String email) {
        this.email = email;
    }

    // Getters et Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}