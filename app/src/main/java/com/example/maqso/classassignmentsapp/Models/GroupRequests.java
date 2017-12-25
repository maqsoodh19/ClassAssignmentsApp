package com.example.maqso.classassignmentsapp.Models;

import com.google.gson.annotations.SerializedName;

public class GroupRequests {

    @SerializedName("student_name")
    private String studentName;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("group_id")
    private int groupId;

    @SerializedName("group_name")
    private String groupName;

    @SerializedName("teacher_id")
    private int teacherId;

    @SerializedName("student_id")
    private int studentId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("id")
    private int id;

    @SerializedName("status")
    private String status;

    public GroupRequests(int groupId) {
        this.groupId = groupId;
    }

    public GroupRequests(int groupId, int studentId) {
        this.groupId = groupId;
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
                "GroupRequests{" +
                        "student_name = '" + studentName + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",group_id = '" + groupId + '\'' +
                        ",group_name = '" + groupName + '\'' +
                        ",teacher_id = '" + teacherId + '\'' +
                        ",student_id = '" + studentId + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",id = '" + id + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}