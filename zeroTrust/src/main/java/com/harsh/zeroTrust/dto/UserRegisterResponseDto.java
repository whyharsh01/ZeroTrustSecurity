package com.harsh.zeroTrust.dto;

public class UserRegisterResponseDto {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String username;
    private String message;


}
