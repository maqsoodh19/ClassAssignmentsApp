package com.example.maqso.classassignmentsapp.Adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maqso.classassignmentsapp.AssignmentActivity;
import com.example.maqso.classassignmentsapp.Fragments.AssignmentDetailFragment;
import com.example.maqso.classassignmentsapp.Models.Assignments;
import com.example.maqso.classassignmentsapp.Models.SectionGroups;
import com.example.maqso.classassignmentsapp.PojoClasses.MyEvents;
import com.example.maqso.classassignmentsapp.R;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;
import com.example.maqso.classassignmentsapp.TeacherHomeActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by maqso on 12/1/2017.
 */

public class AssignmentViewAdapter extends RecyclerView.Adapter<AssignmentViewAdapter.ViewH> {

    ArrayList<Assignments> list;
    Context context;

    public AssignmentViewAdapter(Context context, ArrayList<Assignments> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .assigment_item_view_layout, parent, false);

        return new ViewH(view);
    }

    @Override
    public void onBindViewHolder(ViewH holder, final int position) {
        holder.name.setText(list.get(position).getAssignmentName());
        holder.section.setText("Date: " + list.get(position).getCreatedAt());
        holder.imgBtn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("http://10.0.2.2:8000/" + list.get(position).getAssignmentLink());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewH extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, section;
        ImageButton imgBtn_download;

        public ViewH(final View view) {
            super(view);
            view.setOnClickListener(this);
            name = view.findViewById(R.id.txt_assignment_name);
            section = view.findViewById(R.id.txt_assignment_date);
            imgBtn_download = view.findViewById(R.id.imgBtn_assignment_download);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.poppup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.delete)
                                deleteAssignment(v, getAdapterPosition());
                            if (item.getItemId() == R.id.update)
                                updateGroup(getAdapterPosition());
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
            AssignmentDetailFragment fragment = new AssignmentDetailFragment();
            fragment.dataSet(new MyEvents.AssignmentInfo(list.get(getAdapterPosition())));

            FragmentActivity activity = (FragmentActivity) context;
            activity.getSupportFragmentManager().beginTransaction()
                    .add(R.id.assignment_fragment, fragment)
                    .addToBackStack(null).commit();
        }
    }

    private void updateGroup(int position) {

    }

    // Search bar on recycler view method
    public void setfilter(ArrayList<Assignments> newList) {
        list = new ArrayList<Assignments>();
        list.addAll(newList);
        notifyDataSetChanged();

    }

    private void deleteAssignment(View v, final int position) {
        final Assignments g = list.get(position);
        list.remove(position);
        AssignmentActivity.list.remove(g);
        notifyItemRemoved(position);
        Snackbar snackbar = Snackbar.make(v, g.getAssignmentName() + " This Assignment will be deleted!",
                Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.add(position, g);
                        notifyItemInserted(position);
                        Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                    }
                }).setActionTextColor(context.getResources().getColor(R.color.blue));

        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event != DISMISS_EVENT_ACTION) {
                    Call<ResponseBody> call = ServiceSingleton.getInstance()
                            .deleteAssignment(g.getId(), "Bearer " + TeacherHomeActivity.api_token);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            Log.d("MYTAG", "onResponse: deleted Assignmnt ");
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("MYTAG", "on failure:not Assignment group " + t.getLocalizedMessage());
                        }
                    });
                }
            }

        });
        snackbar.show();
    }


}
