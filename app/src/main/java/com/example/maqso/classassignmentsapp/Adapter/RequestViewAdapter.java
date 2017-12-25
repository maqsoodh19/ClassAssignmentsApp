package com.example.maqso.classassignmentsapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maqso.classassignmentsapp.Models.GroupRequests;
import com.example.maqso.classassignmentsapp.R;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by maqso on 12/1/2017.
 */

public class RequestViewAdapter extends RecyclerView.Adapter<RequestViewAdapter.ViewH> {

    ArrayList<GroupRequests> list;
    Context context;
    String api_token;

    public RequestViewAdapter(Context context, ArrayList<GroupRequests> list, String api_token) {
        this.context = context;
        this.list = list;
        this.api_token = api_token;
    }

    @Override
    public ViewH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_request_item_view_layout,
                parent, false);

        return new ViewH(view);
    }

    @Override
    public void onBindViewHolder(ViewH holder, int position) {
        holder.txtstdName.setText(list.get(position).getStudentName());
        holder.txtgrpName.setText("Group to follow: " + list.get(position).getGroupName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewH extends RecyclerView.ViewHolder {

        TextView txtstdName, txtgrpName;
        Button btnAccept, btnReject;

        public ViewH(View view) {
            super(view);
            txtstdName = view.findViewById(R.id.txt_groupRequest_stdName);
            txtgrpName = view.findViewById(R.id.txt_groupRequest_grpName);
            btnAccept = view.findViewById(R.id.btn_groupRequest_accept);
            btnReject = view.findViewById(R.id.btn_groupRequest_reject);

//            // accept on click
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupRequests req = new GroupRequests(list.get(getAdapterPosition()).getGroupId()
                            , list.get(getAdapterPosition()).getStudentId());
                    Call<GroupRequests> call = ServiceSingleton.getInstance().groupRequestsAccept(req, "Bearer " + api_token);
                    call.enqueue(new Callback<GroupRequests>() {
                        @Override
                        public void onResponse(Call<GroupRequests> call, Response<GroupRequests> response) {
                            if (response.isSuccessful()) {
                                list.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        }

                        @Override
                        public void onFailure(Call<GroupRequests> call, Throwable t) {
                            Toast.makeText(context, "Internet Connection problem", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            // reject on click
            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupRequests req = new GroupRequests(list.get(getAdapterPosition()).getGroupId()
                            , list.get(getAdapterPosition()).getStudentId());
                    Call<GroupRequests> call = ServiceSingleton.getInstance().groupRequestsReject(req, "Bearer " + api_token);
                    call.enqueue(new Callback<GroupRequests>() {
                        @Override
                        public void onResponse(Call<GroupRequests> call, Response<GroupRequests> response) {
                            if (response.isSuccessful()) {
                                list.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        }

                        @Override
                        public void onFailure(Call<GroupRequests> call, Throwable t) {
                            Toast.makeText(context, "Internet Connection problem", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
