package com.winter.network.request;

public class LoginRequest {
    private String login;
    private String password;

    public LoginRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
