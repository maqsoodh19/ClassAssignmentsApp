package com.example.maqso.classassignmentsapp.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.maqso.classassignmentsapp.LoginActivity;
import com.example.maqso.classassignmentsapp.Models.LoginUser;
import com.example.maqso.classassignmentsapp.PathUtils;
import com.example.maqso.classassignmentsapp.R;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;
import com.example.maqso.classassignmentsapp.StudentHomeActivity;
import com.example.maqso.classassignmentsapp.TeacherHomeActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserUpdateFragment extends Fragment {


    private static final String TAG = "MYTAG";

    public UserUpdateFragment() {
        // Required empty public constructor
    }


    View view;
    ProgressDialog pdialog;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    EditText txtName, txtEmail;
    ImageView imgUserPic;
    ImageButton imgBtnClose;
    Button btnUpdate, btnDelete;
    Uri uriImage;
    Boolean delete = false;
    public static final int myRequestCode = 450;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_update, container, false);
        sp = view.getContext().getApplicationContext().getSharedPreferences("USER_CREDIENTIAL", MODE_PRIVATE);
        pdialog = getPdialog();
        if (sp.getString("USER_ROLE", "").equals("teacher")) {
            TeacherHomeActivity.toolbar.setVisibility(View.GONE);
            TeacherHomeActivity.fab.setVisibility(View.GONE);
        } else {
            StudentHomeActivity.toolbar.setVisibility(View.GONE);
        }

        txtName = view.findViewById(R.id.txt_update_userName);
        txtEmail = view.findViewById(R.id.txt_update_userEmail);
        imgUserPic = view.findViewById(R.id.img_update_userPic);
        imgBtnClose = view.findViewById(R.id.imgBtn_update_close);
        btnUpdate = view.findViewById(R.id.btn_update_updateBtn);
        btnDelete = view.findViewById(R.id.btn_update_deleteAcnt);
        infoDisplay();
        closeButton();
        imageSelect();
        updateUser();
        deleteUser();
        return view;
    }


    // image select intent
    private void imageSelect() {
        imgUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, myRequestCode);

            }
        });
    }

    // getting image from intent for result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == myRequestCode && resultCode == RESULT_OK && data != null) {
            uriImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uriImage);
                imgUserPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // update user nam and photo
    private void updateUser() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uriImage != null) {  // ready file to upload
                    File originalFile = new File(PathUtils.getPath(getContext().getApplicationContext(), uriImage));
                    Log.d(TAG, "onClick: file " + originalFile.getAbsolutePath() + " uri path " + uriImage.getPath());
                    RequestBody requestFile = RequestBody.create(
                            MediaType.parse(getActivity().getContentResolver().getType(uriImage)), originalFile
                    );
                    MultipartBody.Part body = MultipartBody.Part.createFormData("avatar",
                            originalFile.getName(), requestFile);

                    Call<LoginUser> call = ServiceSingleton.getInstance().updateUserPic(
                            body, "Bearer " + sp.getString("API_TOKEN", ""));
                    pdialog.show();
                    call.enqueue(new Callback<LoginUser>() {
                        @Override
                        public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "onResponse: suceess " + response.body().getAvatar());
                                editor = sp.edit();
                                editor.putString("USER_AVATAR", response.body().getAvatar());
                                editor.apply();
                                pdialog.dismiss();
                                Intent intent = getActivity().getIntent();
                                getActivity().finish();
                                startActivity(intent);

                            } else if (response.code() == 400) {
                                pdialog.dismiss();
                                Log.d(TAG, "onResponse: 400 " + response.message());
                            } else {
                                pdialog.dismiss();
                                Log.d(TAG, "onResponse: else ");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginUser> call, Throwable t) {
                            pdialog.dismiss();
                            Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                            Toast.makeText(getContext(), "Image Path error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                if (!sp.getString("USER_NAME", "").equals(txtName.getText().toString())) {
                    LoginUser loginUser = new LoginUser(txtName.getText().toString());
                    Call<LoginUser> call = ServiceSingleton.getInstance().updateUser(loginUser,
                            "Bearer " + sp.getString("API_TOKEN", ""));
                    pdialog.show();
                    call.enqueue(new Callback<LoginUser>() {
                        @Override
                        public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                            if (response.isSuccessful()) {
                                editor = sp.edit();
                                editor.putString("USER_NAME", response.body().getUsername());
                                editor.apply();
                                pdialog.dismiss();
                                Intent intent = getActivity().getIntent();
                                getActivity().finish();
                                startActivity(intent);

                            } else if (response.code() == 400) {
                                pdialog.dismiss();
                                Log.d(TAG, "onResponse: 400" + response.message());

                            } else {
                                pdialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginUser> call, Throwable t) {
                            pdialog.dismiss();
                            Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                        }
                    });

                }

            } // on click
        });

    }

    // fragment close back to activity
    private void closeButton() {
        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    // display user inormation in fragments
    private void infoDisplay() {
        txtName.setText(sp.getString("USER_NAME", ""));
        txtEmail.setText(sp.getString("USER_EMAIL", ""));
        String avatar_link = "http://10.0.2.2:8000/" + sp.getString("USER_AVATAR", "");
        Picasso.with(view.getContext()).load(avatar_link).placeholder(R.drawable.default_image).into(imgUserPic);

    }

    //  deleteing user
    private void deleteUser() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Call<ResponseBody> call = ServiceSingleton.getInstance()
                        .deleteAccount("Bearer " + sp.getString("API_TOKEN", ""));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            delete = true;
                        } else if (response.code() == 400) {
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Snackbar.make(v, "Internet Problem", Snackbar.LENGTH_SHORT).setAction("", null).show();

                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (sp.getString("USER_ROLE", "").equals("teacher")) {
            TeacherHomeActivity.toolbar.setVisibility(View.VISIBLE);
            TeacherHomeActivity.fab.setVisibility(View.VISIBLE);
            if (delete) {
                editor = sp.edit();
                editor.clear();
                editor.apply();
            }
        } else {
            StudentHomeActivity.toolbar.setVisibility(View.VISIBLE);
            if (delete) {
                editor = sp.edit();
                editor.clear();
                editor.apply();
            }
        }
        super.onDestroyView();
    }

    // dialog waiting please..
    public ProgressDialog getPdialog() {
        ProgressDialog pDialog = new ProgressDialog(getContext(), R.style.AlertDialogCustom);
        pDialog.setMessage("Please wait..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        return pDialog;
    }
}
