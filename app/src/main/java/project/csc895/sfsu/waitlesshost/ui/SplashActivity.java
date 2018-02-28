package project.csc895.sfsu.waitlesshost.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Chrissy on 2/22/18.
 */

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    private String loginEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SharedPreference to Store API Result
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CachedResponse", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();

        loginEmail = pref.getString("loginEmail", null);

        if (loginEmail != null) {
            //It means User is already Logged in so the user will be taken to MainActivity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over. Start the MainActivity
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("Email", loginEmail);
                    startActivity(intent);
                    // close splash activity
                    finish();
                }
            }, SPLASH_TIME_OUT); // Showing splash screen with a timer

        } else {
            //It means User is not Logged in so the user will be taken to Login Screen
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }

}
