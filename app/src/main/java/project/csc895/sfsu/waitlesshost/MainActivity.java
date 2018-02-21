package project.csc895.sfsu.waitlesshost;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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


    }
}
