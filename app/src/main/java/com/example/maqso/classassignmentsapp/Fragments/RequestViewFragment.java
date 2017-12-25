package com.example.maqso.classassignmentsapp.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.example.maqso.classassignmentsapp.Adapter.RequestViewAdapter;
import com.example.maqso.classassignmentsapp.Models.GroupRequests;
import com.example.maqso.classassignmentsapp.R;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;
import com.example.maqso.classassignmentsapp.TeacherHomeActivity;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestViewFragment extends Fragment {

    private static final String TAG = "MYTAG";
    RecyclerView recyclerView;
    RequestViewAdapter adapter;
    ArrayList<GroupRequests> list;
    String api_token;
    SharedPreferences sp;
    TextView txtNOReq;

    public RequestViewFragment() {
        // Required empty public constructor
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_request_view, container, false);
        list = new ArrayList<>();
        sp = getContext().getSharedPreferences("USER_CREDIENTIAL", Context.MODE_PRIVATE);
        api_token = sp.getString("API_TOKEN", "");
        txtNOReq = view.findViewById(R.id.txt_fragment_request_no_req);
        groupRequests();
        Log.d(TAG, "onCreateView: ");
        TeacherHomeActivity.searchView.setVisibility(View.GONE);
        TeacherHomeActivity.fab.setVisibility(View.GONE);
        TeacherHomeActivity.requestView.setClickable(false);
        TeacherHomeActivity.requestView.setBackgroundColor(getResources().getColor(R.color.primaryDarkColor));
        return view;
    }

    // recycler handling
    private void requestGroupRecycler() {
        recyclerView = view.findViewById(R.id.requestRecyclerView);
        recyclerView.setItemAnimator(new LandingAnimator());
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        adapter = new RequestViewAdapter(getContext(), list, api_token);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ScaleInAnimationAdapter animAdapter = new ScaleInAnimationAdapter(adapter);
        animAdapter.setInterpolator(new OvershootInterpolator());
        animAdapter.setDuration(1000);
        animAdapter.setFirstOnly(false);
        recyclerView.setAdapter(new AlphaInAnimationAdapter(animAdapter));
    }


    // request to server for newly notifications
    private void groupRequests() {
        Log.d(TAG, "groupRequests: ");
        Call<List<GroupRequests>> listCall = ServiceSingleton.getInstance().getGroupRequests("Bearer " + api_token);
        listCall.enqueue(new Callback<List<GroupRequests>>() {
            @Override
            public void onResponse(Call<List<GroupRequests>> call, Response<List<GroupRequests>> response) {
                if (response.isSuccessful()) {    // successful responce
                    List<GroupRequests> clist = response.body();
                    for (GroupRequests x : clist) {
                        list.add(x);
                        Log.d(TAG, "onResponse: " + x + "\n");
                    }
                    TeacherHomeActivity.requestUpdateBadge(list.size());
                    if (list.isEmpty()) {
                        txtNOReq.setVisibility(View.VISIBLE);
                    } else {
                        txtNOReq.setVisibility(View.INVISIBLE);
                        requestGroupRecycler();
                    }
                    //  EventBus.getDefault().post(user);

                } else if (response.code() == 401) {
                    Log.d(TAG, "onResponse: 404 " + response.message());
                } else {
                    Log.d(TAG, "onResponse: else" + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<GroupRequests>> call, Throwable t) {
                Log.d(TAG, "onFailure: internet problems");
            }
        });
    }

    @Override
    public void onDestroyView() {
        TeacherHomeActivity.requestView.setClickable(true);
        TeacherHomeActivity.requestView.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        TeacherHomeActivity.searchView.setVisibility(View.VISIBLE);
        TeacherHomeActivity.fab.setVisibility(View.VISIBLE);
        super.onDestroyView();
    }
}
