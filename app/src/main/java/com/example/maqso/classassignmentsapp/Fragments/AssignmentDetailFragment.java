package com.example.maqso.classassignmentsapp.Fragments;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.maqso.classassignmentsapp.PojoClasses.MyEvents;
import com.example.maqso.classassignmentsapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentDetailFragment extends Fragment {


    public AssignmentDetailFragment() {
        // Required empty public constructor
    }


    View view;
    TextView txtMarks, txtdescription, txtdate;
    ImageButton imgBtnDownload;
    String marks, date, description, link;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_assignment_detail, container, false);
        txtMarks = view.findViewById(R.id.txt_detail_assign_marks);
        txtdescription = view.findViewById(R.id.txt_detail_assign_desription);
        txtdate = view.findViewById(R.id.txt_detail_assign_date);
        imgBtnDownload = view.findViewById(R.id.imgBtn_detail_assign_download);
        txtMarks.setText(marks+"");
        txtdate.setText(date+"");
        txtdescription.setText(description);
        downloadButton();

        return view;
    }

    private void downloadButton() {
        imgBtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("http://10.0.2.2:8000/" + link);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);
            }
        });
    }


    public void dataSet(MyEvents.AssignmentInfo event) {
        marks = event.getAssignments().getAssignmentMarks();
        date = event.getAssignments().getCreatedAt();
        description = event.getAssignments().getAssignmentDescription();
        link = event.getAssignments().getAssignmentLink();

    }

}
