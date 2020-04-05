package com.winter.network.request;

public class RegisterRequest {
    private String companyName;
    private String login;
    private String password;

    public RegisterRequest(String companyName, String login, String password) {
        this.companyName = companyName;
        this.login = login;
        this.password = password;
    }

    public RegisterRequest() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
}
