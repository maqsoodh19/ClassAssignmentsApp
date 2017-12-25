package com.example.maqso.classassignmentsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maqso.classassignmentsapp.Adapter.AssignmentViewAdapter;
import com.example.maqso.classassignmentsapp.Models.Assignments;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignmentActivityStd extends AppCompatActivity {

    private static final String TAG = "MYTAG";
    RecyclerView recyclerView;
    TextView txtNoAssignmentYet;
    AssignmentViewAdapter adapter;
    ArrayList<Assignments> list;
    SharedPreferences sp;
    String api_token;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_std);
        txtNoAssignmentYet = findViewById(R.id.txt_assignment_home_no_assign);
        Toolbar toolbar = findViewById(R.id.tb);
        list = new ArrayList<>();
        sp = getSharedPreferences("USER_CREDIENTIAL", MODE_PRIVATE);
        api_token = sp.getString("API_TOKEN", "");
        id = this.getIntent().getIntExtra("GROUP_ID", 0);
        String title = this.getIntent().getStringExtra("GROUP_NAME");
        Log.d(TAG, "onCreate Assignmen id: " + id);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        assignmentRequest();

        // assignmentRecycler();
    }

    // Tool bar item icon setting
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.teacher_home, menu);
        // notification icon menu
        final MenuItem requestItem = menu.findItem(R.id.menu_request);
        View requestView = requestItem.getActionView();
        requestItem.setVisible(false);
        requestView.setVisibility(View.VISIBLE);
        // search item setting
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Assignments> newList = new ArrayList<>();
                newText = newText.toLowerCase();
                for (Assignments sc : list) {
                    String name = sc.getAssignmentName().toLowerCase();
                    if (name.contains(newText)) {
                        newList.add(sc);
                    }
                }
                adapter.setfilter(newList);
                return true;
            }
        });


        return true;
    }


    // recycler handling
    private void assignmentRecycler() {
        recyclerView = findViewById(R.id.rv);
        recyclerView.setItemAnimator(new LandingAnimator());
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        adapter = new AssignmentViewAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter.setGroupRecyclerClickListener(this);
        ScaleInAnimationAdapter animAdapter = new ScaleInAnimationAdapter(adapter);
        animAdapter.setInterpolator(new OvershootInterpolator());
        animAdapter.setDuration(1000);
        animAdapter.setFirstOnly(false);
        recyclerView.setAdapter(new AlphaInAnimationAdapter(animAdapter));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouch);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    // swipe recycler view item
    ItemTouchHelper.SimpleCallback itemTouch = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper
            .LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Toast.makeText(AssignmentActivityStd.this, "swipe", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
    };

    private void assignmentRequest() {
        Call<List<Assignments>> listCall = ServiceSingleton.getInstance().getAssignments(id, "Bearer " + api_token);
        listCall.enqueue(new Callback<List<Assignments>>() {
            @Override
            public void onResponse(Call<List<Assignments>> call, Response<List<Assignments>> response) {
                if (response.isSuccessful()) {    // successful responce
                    List<Assignments> clist = response.body();
                    for (Assignments x : clist) {
                        list.add(x);
                        Log.d(TAG, "onResponse: " + x + "\n");
                    }
                    if (list.isEmpty()) {
                        txtNoAssignmentYet.setVisibility(View.VISIBLE);
                    } else {
                        txtNoAssignmentYet.setVisibility(View.INVISIBLE);
                        assignmentRecycler();
                    }
                    //  EventBus.getDefault().post(user);

                } else if (response.code() == 401) {
                    Log.d(TAG, "onResponse: 404 " + response.message());
                } else {
                    Log.d(TAG, "onResponse: else" + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<Assignments>> call, Throwable t) {
                Toast.makeText(AssignmentActivityStd.this, "Internet Connection Problems", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
