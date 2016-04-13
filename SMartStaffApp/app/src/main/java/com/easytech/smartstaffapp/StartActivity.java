package com.easytech.smartstaffapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.daimajia.numberprogressbar.NumberProgressBar;

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
                goToLoginActivity();
            }
        };

        countDownTimer.start();
    }

    public void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}

