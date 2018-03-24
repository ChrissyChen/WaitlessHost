package project.csc895.sfsu.waitlesshost.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import project.csc895.sfsu.waitlesshost.R;
import project.csc895.sfsu.waitlesshost.model.Restaurant;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private static final String RESTAURANT_CHILD = "restaurants";
    private static final String RESTAURANT_ID_CHILD = "restaurantID";
    private static final String ARGS_RESTAURANT_ID = "restaurantID";
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DrawerLayout mDrawerLayout;
    private TextView drawerName;
    private String restaurantID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get email and restaurantID either from Splash Activity or Login Activity
        Intent intent = getIntent();
        String email = intent.getStringExtra(SplashActivity.EXTRA_EMAIL);
        restaurantID = intent.getStringExtra(SplashActivity.EXTRA_RESTAURANT_ID);
        Log.d(TAG + "restaurant", restaurantID);// sometimes null. todo load id here

        //Get Firebase mFirebaseAuth instance
        mFirebaseAuth = FirebaseAuth.getInstance();

        // set Navigation Drawer icon
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Navigation Drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);

        drawerName = navHeader.findViewById(R.id.drawer_name);
        loadRestaurantNameWithID();  // Load name in drawer header

        TextView drawerEmail = navHeader.findViewById(R.id.drawer_email);
        drawerEmail.setText(email);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // Set action bar title
                        setTitle(menuItem.getTitle());
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        selectFragment(menuItem.getItemId());

                        return true;
                    }
                });

        // show the home fragment when app first launches
        selectFragment(R.id.nav_home);
    }

    private void selectFragment(int menuItemID) {
        switch (menuItemID) {
            case R.id.nav_home:
                pushFragment(new HomeFragment());
                break;
            case R.id.nav_general_settings:
                GeneralFragment f = new GeneralFragment();
                Log.d(TAG, f.toString());
                pushFragment(f);
                break;
            case R.id.nav_table:
                pushFragment(new TableFragment());
                break;
            case R.id.nav_open_hours:
                pushFragment(new OpenHourFragment());
                break;
//            case R.id.nav_menu:
//                pushFragment(new MenuFragment());
//                break;
            case R.id.nav_guest:
                pushFragment(new GuestFragment());
                break;
            case R.id.nav_signout:
                signOut();
                break;
        }
    }

    private void pushFragment(Fragment fragment) {
        if (fragment == null) return;

        // pass data from activity to fragment
        Bundle args = new Bundle();
        if (restaurantID != null) {
            args.putString(ARGS_RESTAURANT_ID, restaurantID);
            fragment.setArguments(args);
        } else {
            Log.d(TAG, "restaurantID is null!");
        }

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (transaction != null) {
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadRestaurantNameWithID() {
        Query query = mDatabase.child(RESTAURANT_CHILD)
                .orderByChild(RESTAURANT_ID_CHILD)
                .equalTo(restaurantID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // show no result view
                if (!dataSnapshot.hasChildren()) {
                    Log.d(TAG, "NO RESULT FOUND!");
                } else {
                    for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
                        Restaurant restaurant = objSnapshot.getValue(Restaurant.class);
                        if (restaurant != null) {
                            String name = restaurant.getName();
                            drawerName.setText(name);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
