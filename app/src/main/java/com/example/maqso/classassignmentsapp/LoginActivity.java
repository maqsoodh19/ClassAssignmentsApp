package com.example.maqso.classassignmentsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.maqso.classassignmentsapp.Models.LoginUser;
import com.example.maqso.classassignmentsapp.Singleton.ServiceSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MYTAG";
    EditText txtUserEmail, txtUsePassword;
    TextView txtErrorSet, txtSignUp, txtForgetPassword;
    Button btnLogin;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_login);
        pdialog = getPdialog();
        txtUserEmail = findViewById(R.id.txt_login_email);
        txtUsePassword = findViewById(R.id.txt_login_password);
        btnLogin = findViewById(R.id.btn_login_login);
        txtErrorSet = findViewById(R.id.txt_login_error_set);
        txtSignUp = findViewById(R.id.txt_login_create_account);
        txtForgetPassword = findViewById(R.id.txt_login_forget_account);
        sp = getSharedPreferences("USER_CREDIENTIAL", MODE_PRIVATE);
        if (!sp.getString("API_TOKEN", "none").equals("none")) {
            startActi(sp.getString("USER_ROLE", ""));
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtUserEmail.getText().toString();
                String pass = txtUsePassword.getText().toString();
                // verifying email or password is valid or not
                if (Validation.isEmailOk(email)) {
                    txtUserEmail.setError("Enter valid email.");
                } else if (Validation.isPasswordOk(pass)) {
                    txtUsePassword.setError("Enter valid Password.");
                } else {
                    pdialog.show();
                    Call<LoginUser> call = ServiceSingleton.getInstance().login(new LoginUser(txtUserEmail.getText().toString(),
                            txtUsePassword.getText().toString()
                    ));
                    // request to server
                    call.enqueue(new Callback<LoginUser>() {
                        @Override
                        public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                            if (response.isSuccessful()) {    // successful responce
                                LoginUser user = response.body();
                                Log.d(TAG, "onResponse: " + user.getApiToken());
                                EventBus.getDefault().post(user);

                            } else if (response.code() == 404) {
                                pdialog.dismiss();
                                txtErrorSet.setText("Email or password is incorrect.");
                                txtErrorSet.setVisibility(View.VISIBLE);
                                Log.d(TAG, "onResponse: 404 " + response.message());
                            } else {
                                pdialog.dismiss();
                                Log.d(TAG, "onResponse: else" + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginUser> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                            pdialog.dismiss();
                        }
                    });
                }
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserResponce(LoginUser loginUser) {
        pdialog.dismiss();
        editor = sp.edit();
        String role = loginUser.getRole();
        editor.putString("API_TOKEN", loginUser.getApiToken());
        editor.putString("USER_NAME", loginUser.getUsername());
        editor.putString("USER_EMAIL", loginUser.getEmail());
        editor.putString("USER_AVATAR", loginUser.getAvatar());
        editor.putString("USER_ROLE", role);
        editor.apply();
        setDeviceToken(loginUser.getApiToken());
        startActi(role);
    }

    private void setDeviceToken(String apiToken) {
        String dToken = sp.getString("DEVICE_TOKEN", "");
        Log.d(TAG, "setDeviceToken:" + dToken);
        Call<ResponseBody> call = ServiceSingleton.getInstance().setDeviceToken(dToken, "Bearer " + apiToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("MYTAG", "onResponse: device token set:status true ");
                } else if (response.code() == 400) {
                    Log.d("MYTAG", "onResponse: device token set: code 400 ");
                } else {
                    Log.d("MYTAG", "onResponse: device token set:respose code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("MYTAG", "onFailure device token set : " + t.getLocalizedMessage());
            }
        });
    }

    public void startActi(String role) {
        String teacher = "teacher";
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