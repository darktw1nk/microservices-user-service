package com.winter.network.response;

public class LoggedInResponse {
    private String status;
    private String token;

    public LoggedInResponse(String status, String token) {
        this.status = status;
        this.token = token;
    }

    public LoggedInResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
