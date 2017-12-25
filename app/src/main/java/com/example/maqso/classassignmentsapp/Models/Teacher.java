package com.example.maqso.classassignmentsapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by maqso on 10/30/2017.
 */

public class Teacher {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("device_token")
    @Expose
    private String device_token;


    public Teacher(String email, String password) {

        this.email = email;
        this.password = password;
    }

    public Teacher(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("role")
    @Expose
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @SerializedName("avatar")
    @Expose

    private String avatar;
    @SerializedName("email")
    @Expose
    private String email;

    public String getPassword() {
        return password;
    }

    @SerializedName("password")
    @Expose

    private String password;
    @SerializedName("api_token")
    @Expose
    private String api_token;

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    @SerializedName("created_at")

    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
