package com.example.maqso.classassignmentsapp;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.maqso.classassignmentsapp.Models.GroupRequests;
import com.example.maqso.classassignmentsapp.PojoClasses.MyEvents;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by maqso on 12/11/2017.
 */

public class BackgroundTask {

    private static final String TAG = "MYTAG";
    private static String allowStatus = "";
    static Context context;
    public static Boolean status = false;

    // request count to show on badge teacher home activity
    public static void teacherReqCount() {
        try {
            Call<List<GroupRequests>> listCall = ServiceSingleton.getInstance().getGroupRequests("Bearer " + TeacherHomeActivity.api_token);
            listCall.enqueue(new Callback<List<GroupRequests>>() {
                @Override
                public void onResponse(Call<List<GroupRequests>> call, Response<List<GroupRequests>> response) {
                    if (response.isSuccessful()) {    // successful responce
                        List<GroupRequests> clist = response.body();
                        TeacherHomeActivity.requestUpdateBadge(clist.size());

                    } else if (response.code() == 401) {

                    } else {
                    }
                }

                @Override
                public void onFailure(Call<List<GroupRequests>> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "Background Task teacherReqCount: " + e.getLocalizedMessage());
        }

    }

    // getting status for student to have access to group or not
    public static void isAllow(final int groupID, final String name, final Context cont) {
        context = cont;
        try {
            Call<GroupRequests> call = ServiceSingleton.getInstance().groupRequestStatus(new GroupRequests(groupID),
                    "Bearer " + StudentHomeActivity.api_token);
            call.enqueue(new Callback<GroupRequests>() {
                @Override
                public void onResponse(Call<GroupRequests> call, Response<GroupRequests> response) {
                    if (response.isSuccessful()) {    // successful responce
                        GroupRequests groupRequests = response.body();
                        allowStatus = groupRequests.getStatus();
                        if (allowStatus.equals("accepted")) {   // if user allowed access
                            status = true;
                            EventBus.getDefault().post(new MyEvents.GroupAccessEvent(true, groupID, name));
                        } else if (allowStatus.equals("rejected")) { // if user not allowed
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                            alertDialog.setMessage("Your request has been rejected. \n Status: Reject!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                            status = false;
                            EventBus.getDefault().post(new MyEvents.GroupAccessEvent(false, groupID, name));
                        } else if (allowStatus.equals("pending")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                            alertDialog.setMessage("Your request has not approved yet.\n Status: Pending!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                            EventBus.getDefault().post(new MyEvents.GroupAccessEvent(false, groupID, name));
                            status = false;
                        }

                    } else if (response.code() == 404) {
                        Log.d(TAG, "onResponse: 404 " + response.message());
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                        alertDialog.setMessage("Group is private. \n Send a request to follow the group?")
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sendFollowRequest(groupID);
                                    }
                                }).show();
                        status = false;
                        EventBus.getDefault().post(new MyEvents.GroupAccessEvent(false, groupID, name));
                    } else {
                        status = false;
                    }
                }

                @Override
                public void onFailure(Call<GroupRequests> call, Throwable t) {
                    Log.d(TAG, "Background Task student status get: " + t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "Background Task teacherReqCount: " + e.getLocalizedMessage());
        }

        Log.d(TAG, "sendFollowRequest: status " + status);
    }

    public static Boolean permission() {
        return status;
    }

    // new student first time send request to follow
    private static void sendFollowRequest(int groupID) {

        Call<GroupRequests> call = ServiceSingleton.getInstance().sendgroupRequest(new GroupRequests(groupID),
                "Bearer " + StudentHomeActivity.api_token);
        call.enqueue(new Callback<GroupRequests>() {
            @Override
            public void onResponse(Call<GroupRequests> call, Response<GroupRequests> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("pending")) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                        alertDialog.setMessage("Your request has been Send.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }

                } else {
                    Log.d(TAG, "onResponse: else send follow group equest code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GroupRequests> call, Throwable t) {
                Log.d(TAG, "onFailure: erreor" + t.getLocalizedMessage());
            }
        });
    }

}

