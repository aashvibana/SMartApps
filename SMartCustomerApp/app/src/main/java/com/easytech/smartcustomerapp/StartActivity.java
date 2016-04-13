package com.easytech.smartcustomerapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.easytech.smartcustomerapp.pojo.Customer;
import com.google.gson.Gson;

public class StartActivity extends Activity {

    private NumberProgressBar progressBar;
    private final long lengthInMilliseconds = 5000;
    private final long periodInMilliseconds = 48;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        progressBar = (NumberProgressBar) findViewById(R.id.progressBar);
        //progressBar.getProgressDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
        //progressBar.setInterpolator(new LinearInterpolator());
        progressBar.setProgress(0);
        progressBar.setMax(100);

        CountDownTimer countDownTimer = new CountDownTimer(lengthInMilliseconds, periodInMilliseconds) {

            @Override
            public void onTick(long millisUntilFinished) {
                //System.out.print(progressBar.getProgress());
                if(progressBar.getProgress() < 100)
                    progressBar.setProgress(progressBar.getProgress()+1);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(100);
                SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
                String customerJson = sharedpreferences.getString(Constants.Customer, "");
                Gson gson = new Gson();
                Customer customer = gson.fromJson(customerJson, Customer.class);
                if(customer == null || customer.toString() == null) goToLoginActivity();
                else goToMainActivity();
            }
        };

        countDownTimer.start();
    }

    public void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
