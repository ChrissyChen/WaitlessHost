package project.csc895.sfsu.waitlesshost.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Chrissy on 2/22/18.
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "Splash Activity";
    private static final String RESTAURANT_CHILD = "restaurants";
    private static final String EMAIL_CHILD = "email";
    public final static String EXTRA_RESTAURANT_ID = "Pass Restaurant id";
    public final static String EXTRA_EMAIL = "Pass Restaurant email";
    private static int SPLASH_TIME_OUT = 3000;  // Splash screen timer
    private String loginEmail;
    private String restaurantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SharedPreference to Store API Result
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CachedResponse", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();

        loginEmail = pref.getString("loginEmail", null);

        if (loginEmail != null) {      //It means User is already Logged in so the user will be taken to MainActivity
            // get restaurantID
            loadRestaurantIDWithEmail();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over. Start the MainActivity
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra(EXTRA_EMAIL, loginEmail);
                    intent.putExtra(EXTRA_RESTAURANT_ID, restaurantID);
                    startActivity(intent);
                    // close splash activity
                    finish();
                }
            }, SPLASH_TIME_OUT); // Showing splash screen with a timer

        } else {     //It means User is not Logged in so the user will be taken to Login Screen
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

    private void loadRestaurantIDWithEmail() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child(RESTAURANT_CHILD)
                .orderByChild(EMAIL_CHILD)
                .equalTo(loginEmail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // show no result view
                if (!dataSnapshot.hasChildren()) {
                    Log.d(TAG, "NO restaurant FOUND!");
                } else {
                    for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
                        restaurantID = objSnapshot.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
