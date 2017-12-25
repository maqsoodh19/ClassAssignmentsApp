package com.example.maqso.classassignmentsapp.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.maqso.classassignmentsapp.AssignmentActivity;
import com.example.maqso.classassignmentsapp.Models.Assignments;
import com.example.maqso.classassignmentsapp.PathUtils;
import com.example.maqso.classassignmentsapp.PojoClasses.AssignmentEventBus;
import com.example.maqso.classassignmentsapp.R;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;
import com.example.maqso.classassignmentsapp.TeacherHomeActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentCreateFragment extends Fragment {


    private static final String TAG = "MYTAG";

    public AssignmentCreateFragment() {
        // Required empty public constructor
    }

    View view;
    EditText txtname, txtmarks, txtDescription;
    TextView txtSelectedFileName;
    ImageButton imgBtnFileSelect;
    Button btnCancel, btnUpload;
    private static final int requestCode1 = 755;
    Uri uriFile;
    int groupID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_assignment_create, container, false);
        groupID = AssignmentActivity.group_id;
        txtname = view.findViewById(R.id.txt_fragment_assignment_name);
        txtmarks = view.findViewById(R.id.txt_fragment_assignment_marks);
        txtDescription = view.findViewById(R.id.txt_fragment_assignment_desription);
        txtSelectedFileName = view.findViewById(R.id.txt_fragment_assignment_seletedFileName);
        imgBtnFileSelect = view.findViewById(R.id.imgBtn_fragment_assignment_UploadBtn);
        btnCancel = view.findViewById(R.id.btn_fragment_assignmentCreate_Cancel);
        btnUpload = view.findViewById(R.id.btn_fragment_assignmentCreate_Upload);
        cancelButton();
        fileSelect();
        uploadButton();


        return view;
    }


    private void fileSelect() {
        imgBtnFileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, requestCode1);
            }
        });
    }

    // getting image from intent for result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode1 && resultCode == RESULT_OK && data != null) {
            uriFile = data.getData();
            try {
                File file = new File(uriFile.getPath());
                txtSelectedFileName.setText("" + file.getName());
                txtSelectedFileName.setTextColor(getResources().getColor(R.color.sky));
                Log.d(TAG, "onActivityResult: file path: " + file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadButton() {
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriFile != null) {
                    File file = new File(PathUtils.getPath(getContext(), uriFile));
                    final RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver()
                            .getType(uriFile)), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("assignment_link",
                            file.getName(), requestFile);
                    RequestBody name = RequestBody.create(MultipartBody.FORM, txtname.getText().toString());
                    RequestBody description = RequestBody.create(MultipartBody.FORM, txtDescription.getText().toString());
                    RequestBody marks = RequestBody.create(MultipartBody.FORM, txtmarks.getText().toString());
                    RequestBody id = RequestBody.create(MultipartBody.FORM, groupID + "");

                    Call<Assignments> call = ServiceSingleton.getInstance().createAssignment(
                            name, description, marks, id, body, "Bearer " + TeacherHomeActivity.api_token
                    );
                    // execuate the call
                    call.enqueue(new Callback<Assignments>() {
                        @Override
                        public void onResponse(Call<Assignments> call, Response<Assignments> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "onResponse: responce code: " + response.body().getAssignmentLink());
                                EventBus.getDefault().post(new AssignmentEventBus(response.body().getAssignmentName()));
                                getActivity().onBackPressed();

                            } else if (response.code() != 200) {
                                Log.d(TAG, "onResponse: responce code: " + response.code());
                            }

                        }

                        @Override
                        public void onFailure(Call<Assignments> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                        }
                    });

                }
            }
        });

    }

    // back to main actvity
    private void cancelButton() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }


}
