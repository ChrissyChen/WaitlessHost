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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import project.csc895.sfsu.waitlesshost.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DrawerLayout mDrawerLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set Navigation Drawer icon
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // show the home fragment when app first launches
        selectFragment(R.id.nav_home);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

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

//        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
//        btnSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signOut();
//            }
//        });


        // Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View navHeader = navigationView.getHeaderView(0);
        TextView drawerEmail = (TextView) navHeader.findViewById(R.id.drawer_email);
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


    }

    private void selectFragment(int menuItemID) {
        switch (menuItemID) {
            case R.id.nav_home:
                pushFragment(new HomeFragment());
                break;
            case R.id.nav_general_settings:
                pushFragment(new GeneralFragment());
                break;
            case R.id.nav_table:
                pushFragment(new TableFragment());
                break;
            case R.id.nav_open_hours:
                pushFragment(new OpenHourFragment());
                break;
            case R.id.nav_menu:
                pushFragment(new MenuFragment());
                break;
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
}
