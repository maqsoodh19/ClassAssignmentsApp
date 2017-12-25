package com.example.maqso.classassignmentsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maqso.classassignmentsapp.Adapter.GroupViewAdapter;
import com.example.maqso.classassignmentsapp.Fragments.GroupDialogFragment;
import com.example.maqso.classassignmentsapp.Fragments.GroupUpdateDialogFragment;
import com.example.maqso.classassignmentsapp.Fragments.RequestViewFragment;
import com.example.maqso.classassignmentsapp.Fragments.UserUpdateFragment;
import com.example.maqso.classassignmentsapp.Models.SectionGroups;
import com.example.maqso.classassignmentsapp.PojoClasses.GroupInfo;
import com.example.maqso.classassignmentsapp.PojoClasses.GroupRefresh;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MYTAG";
    static TextView txtBadgeView, txtNoGroupYet;
    public static View requestView;
    public static SearchView searchView;
    public static FloatingActionButton fab;
    public static Toolbar toolbar;
    RecyclerView recyclerView;
    GroupViewAdapter adapter;
    public static ArrayList<SectionGroups> list;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView txtUserName, txtUserEmail;
    ImageView imgTeacherAvatar;
    public static String api_token;
    ViewGroup mainActivityView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        mainActivityView = findViewById(R.id.contentFragment);
        EventBus.getDefault().register(this);
        list = new ArrayList<>();
        sp = getSharedPreferences("USER_CREDIENTIAL", MODE_PRIVATE);
        api_token = sp.getString("API_TOKEN", "");
        txtNoGroupYet = findViewById(R.id.txt_teacher_home_no_group);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestGroups();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GroupDialogFragment().show(getSupportFragmentManager(), "Group Dialog Fragment");
            }
        });
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        txtUserName = headerView.findViewById(R.id.txt_teacher_home_name);
        txtUserEmail = headerView.findViewById(R.id.txt_teacher_home_email);
        imgTeacherAvatar = headerView.findViewById(R.id.img_teacher_home_image);
        txtUserName.setText(sp.getString("USER_NAME", ""));
        txtUserEmail.setText(sp.getString("USER_EMAIL", ""));
        String avatar_link = "http://10.0.2.2:8000/" + sp.getString("USER_AVATAR", "");
        Picasso.with(this).load(avatar_link).placeholder(R.drawable.default_image).into(imgTeacherAvatar);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .add(R.id.contentFragment, new UserUpdateFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
            }
        });

    }


    // navigation close on back button presssed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // Tool bar item icon setting
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_home, menu);
        // notification icon menu
        final MenuItem requestItem = menu.findItem(R.id.menu_request);
        requestView = requestItem.getActionView();
        // search item setting
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        // search setting
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<SectionGroups> newList = new ArrayList<>();
                newText = newText.toLowerCase();
                for (SectionGroups sc : list) {
                    String name = sc.getSectionGroupName().toLowerCase();
                    if (name.contains(newText)) {
                        newList.add(sc);
                    }
                }
                adapter.setfilter(newList);
                return true;
            }
        });

        //  new request notification count
        txtBadgeView = requestView.findViewById(R.id.txt_badge_view);
        requestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().addToBackStack(null)
                        .add(R.id.contentFragment, new RequestViewFragment()).commit();
            }
        });
        BackgroundTask.teacherReqCount();
        return true;
    }


    // navigation item click setting

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_logout) {
            editor = sp.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "logout Successfuly", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_manage) {

        }
        Toast.makeText(this, "selected " + id, Toast.LENGTH_SHORT).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //  updating badge count for new requests
    public static void requestUpdateBadge(int no) {
        if (txtBadgeView != null) {
            if (no == 0) {
                if (txtBadgeView.getVisibility() != View.GONE) {
                    txtBadgeView.setVisibility(View.GONE);
                }
            } else {
                txtBadgeView.setText("" + no);
                if (txtBadgeView.getVisibility() != View.VISIBLE) {
                    txtBadgeView.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    // recycler handling
    private void sectionGroupRecycler() {

        recyclerView = findViewById(R.id.sectionRecyclerView);
        recyclerView.setItemAnimator(new LandingAnimator());
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        adapter = new GroupViewAdapter(this, list);
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

    // swipe
    ItemTouchHelper.SimpleCallback itemTouch = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper
            .LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Toast.makeText(TeacherHomeActivity.this, "swipe", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
    };

    // request for group retrieve from server
    private void requestGroups() {
        Call<List<SectionGroups>> listCall = ServiceSingleton.getInstance().getGroupsByTeacher("Bearer " + api_token);
        listCall.enqueue(new Callback<List<SectionGroups>>() {
            @Override
            public void onResponse(Call<List<SectionGroups>> call, Response<List<SectionGroups>> response) {
                Log.d(TAG, "onResponse: " + "skjdnfng" + "\n");
                if (response.isSuccessful()) {    // successful responce
                    List<SectionGroups> clist = response.body();
                    for (SectionGroups x : clist) {
                        list.add(x);
                        Log.d(TAG, "onResponse: " + x + "\n");
                    }
                    if (list.isEmpty()) {
                        txtNoGroupYet.setVisibility(View.VISIBLE);
                    } else {
                        txtNoGroupYet.setVisibility(View.INVISIBLE);
                        sectionGroupRecycler();
                    }
                } else if (response.code() == 401) {
                    Log.d(TAG, "onResponse: 401 " + response.message());
                } else {
                    Log.d(TAG, "onResponse: else" + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<SectionGroups>> call, Throwable t) {
                Snackbar.make(mainActivityView, "Internet Connection Problem!!",
                        Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestGroups();
                    }
                }).setActionTextColor(getResources().getColor(R.color.blue)).show();
            }
        });

    }

    @Subscribe
    public void onGroupCreate(GroupRefresh refresh) {
        list.clear();
        requestGroups();
    }

    @Subscribe
    public void onGroupUpdate(GroupInfo info) {
        GroupUpdateDialogFragment dialog = new GroupUpdateDialogFragment();
        dialog.dataSet(info.getId(), info.getName());
        dialog.show(getSupportFragmentManager(), "Group Dialog Fragment");
    }


}