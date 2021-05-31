package com.bluebitsin.qrscanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bluebitsin.qrscanner.model.QRScanResponse;
import com.bluebitsin.qrscanner.model.User;
import com.bluebitsin.qrscanner.utility.ApiClient;
import com.bluebitsin.qrscanner.utility.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUserId;
    private EditText editTextPassword;
    private Button btnLogin;
    private TextView txtNewRegistration;
    private TextView txtForgotPassword;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        login();


    }

    private void initViews() {

        editTextUserId = (EditText) findViewById(R.id.editTextUserId);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtNewRegistration = (TextView) findViewById(R.id.txtNewRegistration);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
    }


    private void login(){

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = editTextUserId.getText().toString();
                String password = editTextPassword.getText().toString();

                if(userId.trim().isEmpty() && password.trim().isEmpty()){

                    Toast.makeText(LoginActivity.this,
                            "Please provide AgentId and Password.", Toast.LENGTH_SHORT).show();

                }else{

                    // make api call
                    progressDialog.show();
                    getUserData(userId, password);
                }


            }
        });
    }

    //make login api call
    private void getUserData(final String mobile, final String password){

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<User> call = apiService.loginUser(mobile, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                progressDialog.dismiss();
                int responseCode = response.code();

                if(responseCode == 200){
                    // everything ok
                    User user = response.body();
                    // start MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(ParkingConstants.OBJECT_USER, user);
                    startActivity(intent);
                    finish();

                }else if(responseCode == 404){

                    // user not found
                    Toast.makeText(LoginActivity.this, "Invalid AgentId and Password, Please Try Again.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<User>call, Throwable t) {
                // Log error here since request failed
                progressDialog.dismiss();
                Log.e(ParkingConstants.TAG_LOGIN_ACTIVITY, t.toString());
            }
        });
    }

}
