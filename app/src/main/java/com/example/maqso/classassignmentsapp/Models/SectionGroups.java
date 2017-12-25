package com.example.maqso.classassignmentsapp.Models;

import com.google.gson.annotations.SerializedName;


public class SectionGroups{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("section_group_name")
	private String sectionGroupName;

	@SerializedName("group_access")
	private String groupAccess;

	public SectionGroups(String sectionGroupName, String groupAccess) {
		this.sectionGroupName = sectionGroupName;
		this.groupAccess = groupAccess;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSectionGroupName(String sectionGroupName){
		this.sectionGroupName = sectionGroupName;
	}

	public String getSectionGroupName(){
		return sectionGroupName;
	}

	public void setGroupAccess(String groupAccess){
		this.groupAccess = groupAccess;
	}

	public String getGroupAccess(){
		return groupAccess;
	}

	@Override
 	public String toString(){
		return 
			"SectionGroups{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' +
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",section_group_name = '" + sectionGroupName + '\'' + 
			",group_access = '" + groupAccess + '\'' + 
			"}";
		}
}