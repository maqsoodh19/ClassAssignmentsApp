package com.example.maqso.classassignmentsapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maqso.classassignmentsapp.AssignmentActivity;
import com.example.maqso.classassignmentsapp.Fragments.GroupUpdateDialogFragment;
import com.example.maqso.classassignmentsapp.Models.SectionGroups;
import com.example.maqso.classassignmentsapp.PojoClasses.GroupInfo;
import com.example.maqso.classassignmentsapp.R;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;
import com.example.maqso.classassignmentsapp.TeacherHomeActivity;

import org.greenrobot.eventbus.EventBus;

import java.security.acl.Group;
import java.util.ArrayList;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by maqso on 12/1/2017.
 */

public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.ViewH> {

    //    GroupRecyclerClickListener groupRecyclerClickListener;
    ArrayList<SectionGroups> list;
    Context context;

    public GroupViewAdapter(Context context, ArrayList<SectionGroups> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_view_layout, parent, false);

        return new ViewH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewH holder, int position) {
        holder.name.setText(list.get(position).getSectionGroupName());
        holder.section.setText(list.get(position).getGroupAccess() + " group");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewH extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, section;

        public ViewH(final View view) {
            super(view);
            view.setOnClickListener(this);
            name = view.findViewById(R.id.txt_group_name);
            section = view.findViewById(R.id.txt_group_sec_name);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.poppup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.delete)
                                deleteGroup(v, getAdapterPosition());
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
            Intent intent = new Intent(context, AssignmentActivity.class);
            intent.putExtra("GROUP_ID", list.get(getAdapterPosition()).getId());
            intent.putExtra("GROUP_NAME", list.get(getAdapterPosition()).getSectionGroupName() + "");
            context.startActivity(intent);
            notifyItemChanged(getAdapterPosition());
        }
    }

    private void deleteGroup(View v, final int position) {
        final SectionGroups g = list.get(position);
        list.remove(position);
        TeacherHomeActivity.list.remove(g);
        notifyItemRemoved(position);
        Snackbar snackbar = Snackbar.make(v, g.getSectionGroupName() + " This Group will be deleted", Snackbar.LENGTH_LONG)
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
                            .deleteGroup(g.getId(), "Bearer " + TeacherHomeActivity.api_token);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            Log.d("MYTAG", "onResponse: deleted group ");
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("MYTAG", "on failure:not deleted group " + t.getLocalizedMessage());
                        }
                    });
                }
            }

        });
        snackbar.show();
    }

    private void updateGroup(int position) {
        EventBus.getDefault().post(new GroupInfo(list.get(position).getId(), list.get(position).getSectionGroupName(),
                list.get(position).getGroupAccess()));
    }

    // Search bar on recycler view method
    public void setfilter(ArrayList<SectionGroups> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();

    }
}
