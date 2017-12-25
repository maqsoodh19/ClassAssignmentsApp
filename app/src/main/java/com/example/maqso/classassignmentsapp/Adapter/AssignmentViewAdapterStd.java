package com.example.maqso.classassignmentsapp.Adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.maqso.classassignmentsapp.Models.Assignments;
import com.example.maqso.classassignmentsapp.R;

import java.util.ArrayList;

/**
 * Created by maqso on 12/1/2017.
 */

public class AssignmentViewAdapterStd extends RecyclerView.Adapter<AssignmentViewAdapterStd.ViewH> {

    GroupRecyclerClickListener groupRecyclerClickListener;
    ArrayList<Assignments> list;
    Context context;

    public AssignmentViewAdapterStd(Context context, ArrayList<Assignments> list) {
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

        public ViewH(View view) {
            super(view);
            view.setOnClickListener(this);
            name = view.findViewById(R.id.txt_assignment_name);
            section = view.findViewById(R.id.txt_assignment_date);
            imgBtn_download = view.findViewById(R.id.imgBtn_assignment_download);
//            view.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Toast.makeText(context, "long click "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                    list.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());
//                    return true;
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            if (groupRecyclerClickListener != null) {
                groupRecyclerClickListener.GroupRecyclerOnClick(v, getAdapterPosition());
                notifyItemChanged(getAdapterPosition());
            }
        }
    }

    // Search bar on recycler view method
    public void setfilter(ArrayList<Assignments> newList) {
        list = new ArrayList<Assignments>();
        list.addAll(newList);
        notifyDataSetChanged();

    }

    // setting for custom on click listener
    public void setGroupRecyclerClickListener(GroupRecyclerClickListener groupRecyclerClickListener) {
        this.groupRecyclerClickListener = groupRecyclerClickListener;
    }

    public interface GroupRecyclerClickListener {
        public void GroupRecyclerOnClick(View v, int position);
    }
}
