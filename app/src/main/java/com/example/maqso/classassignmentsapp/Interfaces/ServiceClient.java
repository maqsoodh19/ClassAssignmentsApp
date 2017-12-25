package com.example.maqso.classassignmentsapp.Interfaces;

import com.example.maqso.classassignmentsapp.Models.Assignments;
import com.example.maqso.classassignmentsapp.Models.GroupRequests;
import com.example.maqso.classassignmentsapp.Models.LoginUser;
import com.example.maqso.classassignmentsapp.Models.SectionGroups;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


/**
 * Created by maqso on 10/30/2017.
 */

public interface ServiceClient {

    //notification new request to Teacher
    @GET("/api/groupRequest/requests")
    Call<List<GroupRequests>> getGroupRequests(@Header("Authorization") String token);

    @POST("/api/groupRequest/accepted")
    Call<GroupRequests> groupRequestsAccept(@Body GroupRequests req, @Header("Authorization") String token);

    @POST("/api/groupRequest/rejected")
    Call<GroupRequests> groupRequestsReject(@Body GroupRequests req, @Header("Authorization") String token);

    // login api
    @POST("/api/user/login")
    Call<LoginUser> login(@Body LoginUser user);

    // sign up api
    @Headers("Accept: application/json")
    @POST("/api/user/signup")
    Call<LoginUser> signUp(@Body LoginUser user);

    // delete Account user info
    @DELETE("/api/user")
    Call<ResponseBody> deleteAccount(@Header("Authorization") String token);

    // update user info
    @POST("/api/user/update")
    Call<LoginUser> updateUser(@Body LoginUser loginUser, @Header("Authorization") String token);

    // update user pictures
    @Multipart
    @POST("/api/user/update/pic")
    Call<LoginUser> updateUserPic(@Part MultipartBody.Part avatar, @Header("Authorization") String token);

    // group access by teacher
    @GET("/api/groups/teacher")
    Call<List<SectionGroups>> getGroupsByTeacher(@Header("Authorization") String token);

    // group create by teacher
    @POST("/api/groups")
    Call<SectionGroups> createGroup(@Body SectionGroups groups, @Header("Authorization") String token);

    // group access by student
    @GET("/api/groups")
    Call<List<SectionGroups>> getGroups(@Header("Authorization") String token);

    // delete group by teacher
    @DELETE("/api/groups/{u}")
    Call<ResponseBody> deleteGroup(@Path("u") int id, @Header("Authorization") String token);

    // delete group by teacher
    @POST("/api/groups/update/{u}")
    Call<SectionGroups> updateGroup(@Path("u") int id, @Body SectionGroups g, @Header("Authorization") String token);

    // group request status return for student
    @POST("/api/groupRequest/status")
    Call<GroupRequests> groupRequestStatus(@Body GroupRequests req, @Header("Authorization") String token);

    // send request to follow group
    @POST("/api/groupRequest")
    Call<GroupRequests> sendgroupRequest(@Body GroupRequests req, @Header("Authorization") String token);

    // Assignment Api
    @GET("/api/assignment/{id}")
    Call<List<Assignments>> getAssignments(@Path("id") int id, @Header("Authorization") String token);

    // delete group by teacher
    @DELETE("/api/assignment/{u}")
    Call<ResponseBody> deleteAssignment(@Path("u") int id, @Header("Authorization") String token);

    // set deive token
    @POST("/api/user/device_token")
    @FormUrlEncoded
    Call<ResponseBody> setDeviceToken(@Field("device_token") String device_token, @Header("Authorization") String token);

    // create Assignment
    @Multipart
    @POST("/api/assignment")
    Call<Assignments> createAssignment(@Part("assignment_name") RequestBody name,
                                       @Part("assignment_description") RequestBody desription,
                                       @Part("assignment_marks") RequestBody marks,
                                       @Part("group_id") RequestBody group_id,
                                       @Part MultipartBody.Part assignment_link,
                                       @Header("Authorization") String token);
}
