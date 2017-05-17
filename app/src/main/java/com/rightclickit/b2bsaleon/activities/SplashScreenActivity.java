package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

public class SplashScreenActivity extends AppCompatActivity {

    private Runnable mRunnable;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(mRunnable);

                    Intent logInPage = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    logInPage.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(logInPage);
                    finish();
                }
            };

            if (new NetworkConnectionDetector(SplashScreenActivity.this).isNetworkConnected()) {
                mHandler.postDelayed(mRunnable, 4000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
