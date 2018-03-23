package project.csc895.sfsu.waitlesshost.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import project.csc895.sfsu.waitlesshost.R;
import project.csc895.sfsu.waitlesshost.model.Waitlist;
import project.csc895.sfsu.waitlesshost.model.Number;

/**
 * Created by Chrissy on 3/22/18.
 */

public class AssignWaitlistActivity extends AppCompatActivity {

    private static final String TAG = "AssignWaitlistActivity";
    private static final String NUMBER_CHILD = "numbers";
    private static final String TABLE_CHILD = "tables";
    private static final String WAITLIST_CHILD = "waitlists";
    private static final String RESTAURANT_ID_CHILD = "restaurantID";
    private static final String STATUS_CHILD = "status";
    private static final String NUMBER_STATUS_WAITING = "Waiting";
    private static final String NUMBER_STATUS_DINING = "Dining";
    private static final String NUMBER_STATUS_CANCELLED = "Cancelled";
    private static final String NUMBER_STATUS_COMPLETED = "Completed";
    private static final String TABLE_STATUS_SEATED = "Seated";
    private static final String NUMBER_ID_CHILD = "numberID";
    private static final String WAIT_NUM_TABLE_A_CHILD = "waitNumTableA";
    private static final String WAIT_NUM_TABLE_B_CHILD = "waitNumTableB";
    private static final String WAIT_NUM_TABLE_C_CHILD = "waitNumTableC";
    private static final String WAIT_NUM_TABLE_D_CHILD = "waitNumTableD";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView mRecyclerView;
    private TextView mNoRecordTextView;
    private int tableSize;
    private String restaurantID;
    private static String tableName, tableID, waitlistID;
    private static int waitNumTableA, waitNumTableB, waitNumTableC, waitNumTableD;
    private static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_waitlist);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        Intent intent = getIntent();
        restaurantID = intent.getStringExtra(HomeFragment.EXTRA_RESTAURANT_ID);
        tableSize = intent.getIntExtra(HomeFragment.EXTRA_TABLE_SIZE, 0);
        tableID = intent.getStringExtra(HomeFragment.EXTRA_TABLE_ID);
        tableName = intent.getStringExtra(HomeFragment.EXTRA_TABLE_NAME);

        mActivity = AssignWaitlistActivity.this;

        initViews();
        getWaitlistInfo();
        showQualifiedNumberInfo();

    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.waitingGuest);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNoRecordTextView = (TextView) findViewById(R.id.noRecord);
    }

    private void getWaitlistInfo() {
        Query query = mDatabase.child(WAITLIST_CHILD)
                .orderByChild(RESTAURANT_ID_CHILD)
                .equalTo(restaurantID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    Log.d("Error", "NO WAITLIST SHOWS");
                } else {
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Waitlist waitlist = objSnapshot.getValue(Waitlist.class);
                        if (waitlist != null) {
                            waitlistID = waitlist.getWaitlistID();
                            Log.d("waitlistID inside", waitlistID);
                            waitNumTableA = waitlist.getWaitNumTableA();
                            waitNumTableB = waitlist.getWaitNumTableB();
                            waitNumTableC = waitlist.getWaitNumTableC();
                            waitNumTableD = waitlist.getWaitNumTableD();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showQualifiedNumberInfo() {
        Query query = mDatabase.child(NUMBER_CHILD)
                .orderByChild(RESTAURANT_ID_CHILD)
                .equalTo(restaurantID);

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Number, WaitingNumberViewHolder>(
                Number.class,
                R.layout.item_brief_waitlist_info,
                WaitingNumberViewHolder.class,
                query) {
            @Override
            protected void populateViewHolder(WaitingNumberViewHolder viewHolder, Number number, int position) {
                String status = number.getStatus();
                int partySize = number.getPartyNumber();
                if (status.equals(NUMBER_STATUS_WAITING) && partySize <= tableSize) {
                    viewHolder.setCustomerName(number.getUsername());
                    viewHolder.setNumberName(number.getNumberName());
                    viewHolder.setPartySize(String.valueOf(partySize));
                    viewHolder.setStatus(number.getStatus());
                    viewHolder.setCreatedTime(number.getTimeCreated());

                    viewHolder.onClick(number);
                } else {
                    viewHolder.mWaitingGuestCardView.setVisibility(View.GONE);
                }
            }
        };
        mRecyclerView.setAdapter(adapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // show no result view
                if (!dataSnapshot.hasChildren()) {
                    mRecyclerView.setVisibility(View.GONE);
                    mNoRecordTextView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "NO RECORD SHOWS");
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mNoRecordTextView.setVisibility(View.GONE);
                    Log.d(TAG, "RESULT VIEW SHOWS");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class WaitingNumberViewHolder extends RecyclerView.ViewHolder {
        private CardView mWaitingGuestCardView; // to hide not qualified numbers
        private TextView mCustomerName, mNumberName, mPartySize, mStatus, mCreatedTime;

        public WaitingNumberViewHolder(View itemView) {
            super(itemView);
            mWaitingGuestCardView = (CardView) itemView.findViewById(R.id.waitingGuestCardView);
            mCustomerName = (TextView) itemView.findViewById(R.id.customerName);
            mNumberName = (TextView) itemView.findViewById(R.id.numberName);
            mPartySize = (TextView) itemView.findViewById(R.id.partyNumber);
            mStatus = (TextView) itemView.findViewById(R.id.status);
            mCreatedTime = (TextView) itemView.findViewById(R.id.numberCreatedTime);
        }

        public void setCustomerName(String customerName) {
            mCustomerName.setText(customerName);
        }

        public void setNumberName(String numberName) {
            mNumberName.setText(numberName);
        }

        public void setPartySize(String partySize) {
            mPartySize.setText(partySize);
        }

        public void setStatus(String status) {
            mStatus.setText(status);
        }

        public void setCreatedTime(String createdTime) {
            mCreatedTime.setText(createdTime);
        }

        public void onClick(final Number number) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(number);
                }
            });
        }
    }

    private static void showAlertDialog(final Number number) {
        final String numberID = number.getNumberID();
        final String numberName = number.getNumberName();
        String message = "Are you sure to assign Guest " + numberName + " to Table " + tableName + "?";

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(message);
        builder.setCancelable(false); // Disallow cancel of AlertDialog on click of back button and outside touch

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo go back to home page, change status of table and number
                        Toast.makeText(mActivity, "Assignment Complete!", Toast.LENGTH_SHORT).show();
                        mActivity.onBackPressed();  // todo check?
                        updateTableStatus(numberID);
                        updateNumberStatus(numberID, numberName);
                    }
                });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private static void updateTableStatus(String numberID) {
        //Table status: from Open to Seated
        //add numberID indicating current guest
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tableRef = databaseRef.child(TABLE_CHILD).child(tableID);
        tableRef.child(STATUS_CHILD).setValue(TABLE_STATUS_SEATED);
        Log.d(TAG, "Table status change to Seated");

        tableRef.child(NUMBER_ID_CHILD).setValue(numberID);
        Log.d(TAG, "Set numberID");
    }

    private static void updateNumberStatus(String numberID, String numberName) {
        //Number status: from Waiting to Dining
        //update waitlist info
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference numberRef = databaseRef.child(NUMBER_CHILD).child(numberID);
        numberRef.child(STATUS_CHILD).setValue(NUMBER_STATUS_DINING);
        Log.d(TAG, "Number status change to Dining");

        updateWaitlistInfoWhenDining(numberName);
        Log.d(TAG, "Update waitlist wait num");
    }

    private static void updateWaitlistInfoWhenDining(String numberName) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = databaseRef.child(WAITLIST_CHILD).child(waitlistID);
        if (numberName.charAt(0) == 'A') {
            waitNumTableA -= 1;
            ref.child(WAIT_NUM_TABLE_A_CHILD).setValue(waitNumTableA);
        } else if (numberName.charAt(0) == 'B') {
            waitNumTableB -= 1;
            ref.child(WAIT_NUM_TABLE_B_CHILD).setValue(waitNumTableB);
        } else if (numberName.charAt(0) == 'C') {
            waitNumTableC -= 1;
            ref.child(WAIT_NUM_TABLE_C_CHILD).setValue(waitNumTableC);
        } else if (numberName.charAt(0) == 'D') {
            waitNumTableD -= 1;
            ref.child(WAIT_NUM_TABLE_D_CHILD).setValue(waitNumTableD);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}


