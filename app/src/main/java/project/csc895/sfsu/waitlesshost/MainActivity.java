package project.csc895.sfsu.waitlesshost;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import project.csc895.sfsu.waitlesshost.model.Restaurant;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Button btnSignOut;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //Now check if this user is null
        if (mFirebaseUser == null){
            //send user to the login page
        }

        Log.d(TAG, "Main activity");

//       ArrayList<String> cuisineTags = new ArrayList<>();
//       cuisineTags.add("Chinese");
//       cuisineTags.add("Szechuan");
//       Restaurant restaurant = new Restaurant("Hunan Impression", cuisineTags);
//       mDatabase.child("restaurants").push().setValue(restaurant);
//
//       Log.d(TAG, "test to write Restaurant j");

        Intent intent = getIntent();
        String email = intent.getStringExtra("Email");

        TextView testUser = (TextView) findViewById(R.id.test_user);
        TextView testEmail = (TextView) findViewById(R.id.test_email);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        testUser.setText(email);
        testEmail.setText(mFirebaseUser.getEmail());

        Log.d(TAG + "get user", mFirebaseUser.toString());
        //Log.d("get user display name", mFirebaseUser.getDisplayName());
        Log.d("get user email", mFirebaseUser.getEmail());
        //Log.d("get user tel", mFirebaseUser.getPhoneNumber());

    }

    private void signOut() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CachedResponse", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        mFirebaseAuth.signOut();    // check??
        finish();

    }
}
