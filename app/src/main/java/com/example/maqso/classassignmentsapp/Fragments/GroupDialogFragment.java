package com.example.maqso.classassignmentsapp.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maqso.classassignmentsapp.Models.SectionGroups;
import com.example.maqso.classassignmentsapp.PojoClasses.GroupRefresh;
import com.example.maqso.classassignmentsapp.R;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;
import com.example.maqso.classassignmentsapp.TeacherHomeActivity;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by maqso on 12/17/2017.
 */

public class GroupDialogFragment extends AppCompatDialogFragment {

    private static final String TAG = "MYTAG";
    View view;
    ProgressDialog pdialog;
    EditText txtGroupName;
    Spinner spnAccess;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_dialog_group_layout, null);
        txtGroupName = view.findViewById(R.id.txt_fragment_group_create_name);
        spnAccess = view.findViewById(R.id.spn_fragment_dialog_group_access);
        pdialog = getPdialog();
        builder.setView(view)
                .setTitle("Create Group")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing
                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // positive button
                        createGroup();
                    }
                });

        return builder.create();
    }


    private void createGroup() {
        SectionGroups createGroup = new SectionGroups(txtGroupName.getText().toString(),
                spnAccess.getSelectedItem().toString());

        Call<SectionGroups> call = ServiceSingleton.getInstance().createGroup(createGroup, "Bearer " +
                TeacherHomeActivity.api_token);
        pdialog.show();
        call.enqueue(new Callback<SectionGroups>() {
            @Override
            public void onResponse(Call<SectionGroups> call, Response<SectionGroups> response) {
                if (response.isSuccessful()) {
                    pdialog.dismiss();
                    EventBus.getDefault().post(new GroupRefresh(response.body().getSectionGroupName()));
                    Log.d(TAG, "onResponse: " + response.body().getSectionGroupName());
                } else if (response.code() != 200) {
                    pdialog.dismiss();
                    Log.d(TAG, "onResponse: else responce code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SectionGroups> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                pdialog.dismiss();
            }
        });

    }


    public ProgressDialog getPdialog() {
        ProgressDialog pDialog = new ProgressDialog(getContext(), R.style.AlertDialogCustom);
        pDialog.setMessage("Please wait..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        return pDialog;
    }
}
