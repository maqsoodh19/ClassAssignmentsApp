package com.example.maqso.classassignmentsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maqso.classassignmentsapp.AssignmentActivityStd;
import com.example.maqso.classassignmentsapp.BackgroundTask;
import com.example.maqso.classassignmentsapp.Models.SectionGroups;
import com.example.maqso.classassignmentsapp.R;

import java.util.ArrayList;

/**
 * Created by maqso on 12/1/2017.
 */

public class GroupViewAdapterStd extends RecyclerView.Adapter<GroupViewAdapterStd.ViewH> {

    ArrayList<SectionGroups> list;
    public Context context;

    public GroupViewAdapterStd(Context context, ArrayList<SectionGroups> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_view_layout, parent, false);

        return new ViewH(view);
    }

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

        public ViewH(View view) {
            super(view);
            view.setOnClickListener(this);
            name = view.findViewById(R.id.txt_group_name);
            section = view.findViewById(R.id.txt_group_sec_name);
        }

        @Override
        public void onClick(View v) {

            if (list.get(getAdapterPosition()).getGroupAccess().equals("public")) {
                Intent intent = new Intent(context, AssignmentActivityStd.class);
                intent.putExtra("GROUP_ID", list.get(getAdapterPosition()).getId());
                intent.putExtra("GROUP_NAME", list.get(getAdapterPosition()).getSectionGroupName() + "");
                context.startActivity(intent);
                BackgroundTask.status = false;
                notifyItemChanged(getAdapterPosition());
            } else {
                BackgroundTask.isAllow(list.get(getAdapterPosition()).getId(),
                        list.get(getAdapterPosition()).getSectionGroupName(), context);
            }
            notifyItemChanged(getAdapterPosition());
        }

    }

    // Search bar on recycler view method
    public void setfilter(ArrayList<SectionGroups> newList) {
        list = new ArrayList<SectionGroups>();
        list.addAll(newList);
        notifyDataSetChanged();

    }


}
