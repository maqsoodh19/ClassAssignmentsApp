package com.example.maqso.classassignmentsapp.Models;

import com.google.gson.annotations.SerializedName;

public class LoginUser {

    private String password;
    @SerializedName("role")
    private String role;

    @SerializedName("api_token")
    private String apiToken;

    @SerializedName("device_token")
    private String device_token;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @SerializedName("avatar")

    private String avatar;

    public LoginUser(String username) {
        this.username = username;
    }

    public LoginUser(String username, String email, String password, String role) {
        this.password = password;
        this.role = role;
        this.email = email;
        this.username = username;
    }

    public LoginUser(String email, String password) {
        this.password = password;
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return
                "LoginUser{" +
                        "role = '" + role + '\'' +
                        ",api_token = '" + apiToken + '\'' +
                        ",email = '" + email + '\'' +
                        ",username = '" + username + '\'' +
                        "}";
    }
}