package com.example.maqso.classassignmentsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maqso.classassignmentsapp.Models.LoginUser;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "MYTAG";
    EditText txtuserName, txtEmail, txtPaswword;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ProgressDialog pdialog;
    Button btnSubmit;
    Spinner spnRole;
    String selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sp = getSharedPreferences("USER_CREDIENTIAL", MODE_PRIVATE);
        pdialog = getPdialog();
        txtuserName = findViewById(R.id.txt_signUp_userName);
        txtEmail = findViewById(R.id.txt_signUp_userEmail);
        txtPaswword = findViewById(R.id.txt_signUp_userPassword);
        spnRole = findViewById(R.id.spn_signUp_role);
        btnSubmit = findViewById(R.id.btn_signUp_register);

        spnRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = spnRole.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SignUpActivity.this, "must select", Toast.LENGTH_SHORT).show();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation.isValidUserName(txtuserName.getText().toString())) {
                    txtuserName.setError("Enter User Name");
                    return;
                }
                if (Validation.isEmailOk(txtEmail.getText().toString())) {
                    txtEmail.setError("Enter valid email.");
                    return;
                }
                if (Validation.isPasswordOk(txtPaswword.getText().toString())) {
                    txtPaswword.setError("Enter valid Password");
                    return;
                }

                Call<LoginUser> call = ServiceSingleton.getInstance().signUp(new LoginUser(
                        txtuserName.getText().toString(),
                        txtEmail.getText().toString(),
                        txtPaswword.getText().toString(),
                        selected
                ));
                pdialog.show();
                call.enqueue(new Callback<LoginUser>() {
                    @Override
                    public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                        if (response.isSuccessful()) {
                            LoginUser loginUser = response.body();
                            editor = sp.edit();
                            String role = loginUser.getRole();
                            editor.putString("API_TOKEN", loginUser.getApiToken());
                            editor.putString("USER_NAME", loginUser.getUsername());
                            editor.putString("USER_EMAIL", loginUser.getEmail());
                            editor.putString("USER_AVATAR", loginUser.getAvatar());
                            editor.putString("USER_ROLE", role);
                            editor.apply();
                            startActi(role);

                        } else if (response.code() == 422) {
                            txtEmail.setError("The email has already been taken.");
                            pdialog.dismiss();
                            Log.d(TAG, "onResponse: 422");
                        } else {
                            pdialog.dismiss();
                            Log.d(TAG, "onResponse: 422");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginUser> call, Throwable t) {
                        pdialog.dismiss();
                        Log.d(TAG, "onFailure: sign up " + t.getLocalizedMessage());
                    }
                });

            }
        });

    }


    public void startActi(String role) {
        String teacher = "teacher";
        pdialog.dismiss();
        if (teacher.equals(role)) {
            Intent intent = new Intent(this, TeacherHomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, StudentHomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public ProgressDialog getPdialog() {
        ProgressDialog pDialog = new ProgressDialog(this, R.style.AlertDialogCustom);
        pDialog.setMessage("Please wait..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        return pDialog;
    }
}
