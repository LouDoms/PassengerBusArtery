package com.itproject.piczon.loudoms.passengerbusartery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.itproject.piczon.loudoms.passengerbusartery.data.model.Passenger;
import com.itproject.piczon.loudoms.passengerbusartery.data.remote.APIService;
import com.itproject.piczon.loudoms.passengerbusartery.data.remote.APIUtils;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by LouDoms on 11/29/2017.
 */

public class RegistrationActivity extends AppCompatActivity {

    private static String TAG = "RetrofitApi";

    private Button loginRedirect, register;
    private EditText edtName, edtUsername, edtPassword, edtAge;
    private RadioGroup rgGender;
    private RadioButton rbGender;
    private RadioGroup rgType;
    private RadioButton rbType;
    private CheckBox cbGenerate;

    private APIService apiService;

    Random random;
    private int tagId, balance = 0, inTransit = 0, numberOfSeats = 0;
    private String busNumber = "null", busType= "null", destination = "null";
    private Double longitude = 0.0, latitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        register = (Button) findViewById(R.id.btnRegister);
        loginRedirect = (Button) findViewById(R.id.btnLogInRedirect);
        edtName = (EditText) findViewById(R.id.edtName);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtAge = (EditText) findViewById(R.id.edtAge);

        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rgType = (RadioGroup) findViewById(R.id.rgType);
        cbGenerate = (CheckBox) findViewById(R.id.checkboxGenerate);

        random = new Random();

        apiService = APIUtils.getAPIService();

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                int age = Integer.parseInt(edtAge.getText().toString());

                int genderGroup = rgGender.getCheckedRadioButtonId();
                rbGender = (RadioButton) findViewById(genderGroup);
                String gender = rbGender.getText().toString();

                int typeGroup = rgType.getCheckedRadioButtonId();
                rbType = (RadioButton) findViewById(typeGroup);
                String type = rbType.getText().toString();

                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                tagId = 100000 + random.nextInt(900000);
                String passTagId = String.valueOf(tagId);

                if (password.isEmpty() || password.length() < 6) {
                    edtPassword.setError("Password must at least be 6 characters!");
                } else if (!(cbGenerate.isChecked())) {
                    cbGenerate.setError("NFC Number Generator should be checked!");
                } else {
                    sendPost(name, username, password, gender, age,
                            balance, busNumber, busType, inTransit, type, tagId,
                            destination, numberOfSeats, longitude, latitude);
                }
            }
        });
    }

    public void sendPost(String name, String username, String password, String gender, int age,
                         int balance, String busNumber, String busType, int inTransit, String type,
                         int tagId, String destination, int numberOfSeats,
                         double longitude, double latitude) {

        apiService.savePost(name, username, password, gender, age,
                balance, busNumber, busType, inTransit, type, tagId, destination,
                numberOfSeats, longitude, latitude).enqueue(new Callback<Passenger>() {
            @Override
            public void onResponse(Call<Passenger> call, Response<Passenger> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this,
                            "Successfully Registered your account!", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<Passenger> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Failed Registration. " +
                        "Check your internet connection", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

}
