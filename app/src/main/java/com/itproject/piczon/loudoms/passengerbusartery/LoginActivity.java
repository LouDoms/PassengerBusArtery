package com.itproject.piczon.loudoms.passengerbusartery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itproject.piczon.loudoms.passengerbusartery.data.model.Passenger;
import com.itproject.piczon.loudoms.passengerbusartery.data.remote.APIService;
import com.itproject.piczon.loudoms.passengerbusartery.data.remote.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Headers;

/**
 * Created by LouDoms on 11/29/2017.
 */

public class LoginActivity extends AppCompatActivity{

    Button btnRegisterRedirect, btnLogin;
    EditText edtUsername, edtPassword;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText) findViewById(R.id.edtUsernameLogIn);
        edtPassword = (EditText) findViewById(R.id.edtPasswordLogIn);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegisterRedirect = (Button) findViewById(R.id.btnRegisterRedirect);

        apiService = APIUtils.getAPIService();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        btnRegisterRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });
    }

    public void userLogin(){
        final String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String type = "passenger";

        apiService.postUserLogin(username, password, type).enqueue(new Callback<Passenger>() {
            @Override
            public void onResponse(Call<Passenger> call, Response<Passenger> response) {
                if(response.isSuccessful()){

                    Log.i("TAG", response.message());

                }else{
                    Toast.makeText(getApplicationContext(), "Incorrect login credentials.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Passenger> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}