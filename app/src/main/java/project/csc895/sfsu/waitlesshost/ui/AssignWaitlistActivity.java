package project.csc895.sfsu.waitlesshost.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import project.csc895.sfsu.waitlesshost.R;

/**
 * Created by Chrissy on 3/22/18.
 */

public class AssignWaitlistActivity extends AppCompatActivity {

    private static final String NUMBER_CHILD = "numbers";
    private static final String WAITLIST_CHILD = "waitlists";
    private static final String RESTAURANT_ID_CHILD = "restaurantID";
    private static final String STATUS_CHILD = "status";
    private static final String STATUS_WAITING = "Waiting";
    private static final String STATUS_DINING = "Dining";
    private static final String STATUS_CANCELLED = "Cancelled";
    private static final String STATUS_COMPLETED = "Completed";
    private static final String WAIT_NUM_TABLE_A_CHILD = "waitNumTableA";
    private static final String WAIT_NUM_TABLE_B_CHILD = "waitNumTableB";
    private static final String WAIT_NUM_TABLE_C_CHILD = "waitNumTableC";
    private static final String WAIT_NUM_TABLE_D_CHILD = "waitNumTableD";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String numberID, restaurantID, waitlistID;
    private String numberName;
    private int waitNumTableA, waitNumTableB, waitNumTableC, waitNumTableD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_waitlist);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        Intent intent = getIntent();
        restaurantID = intent.getStringExtra(HomeFragment.EXTRA_RESTAURANT_ID);

//        initViews();
//        loadNumberInfo();
//        getWaitlistInfo();

    }

}
