package com.example.maqso.classassignmentsapp.Models;


import com.google.gson.annotations.SerializedName;

public class Assignments{

	@SerializedName("assignment_marks")
	private String assignmentMarks;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("group_id")
	private int groupId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("assignment_name")
	private String assignmentName;

	@SerializedName("assignment_description")
	private String assignmentDescription;

	@SerializedName("id")
	private int id;

	@SerializedName("assignment_link")
	private String assignmentLink;

	public void setAssignmentMarks(String assignmentMarks){
		this.assignmentMarks = assignmentMarks;
	}

	public String getAssignmentMarks(){
		return assignmentMarks;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setGroupId(int groupId){
		this.groupId = groupId;
	}

	public int getGroupId(){
		return groupId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setAssignmentName(String assignmentName){
		this.assignmentName = assignmentName;
	}

	public String getAssignmentName(){
		return assignmentName;
	}

	public void setAssignmentDescription(String assignmentDescription){
		this.assignmentDescription = assignmentDescription;
	}

	public String getAssignmentDescription(){
		return assignmentDescription;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setAssignmentLink(String assignmentLink){
		this.assignmentLink = assignmentLink;
	}

	public String getAssignmentLink(){
		return assignmentLink;
	}

	@Override
 	public String toString(){
		return 
			"Assignments{" + 
			"assignment_marks = '" + assignmentMarks + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",group_id = '" + groupId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",assignment_name = '" + assignmentName + '\'' + 
			",assignment_description = '" + assignmentDescription + '\'' + 
			",id = '" + id + '\'' + 
			",assignment_link = '" + assignmentLink + '\'' + 
			"}";
		}
}