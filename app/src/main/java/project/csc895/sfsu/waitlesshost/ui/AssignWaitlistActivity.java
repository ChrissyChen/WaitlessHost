package project.csc895.sfsu.waitlesshost.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private RecyclerView mRecyclerView;
    private TextView mNoRecordTextView;
    private int tableSize;
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
        tableSize = intent.getIntExtra(HomeFragment.EXTRA_TABLE_SIZE, 0);

        initViews();
        //getWaitlistInfo();
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
                if (status.equals(STATUS_WAITING) && partySize <= tableSize) {
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
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, NumberDetailedActivity.class);
//                    //intent.putExtra(EXTRA_NUMBER, number);// pass number obj
//
//                    intent.putExtra(EXTRA_NUMBER_ID, number.getNumberID());// pass number id
//                    intent.putExtra(EXTRA_RESTAURANT_ID, number.getRestaurantID());
//                    context.startActivity(intent);
                    // TODO
                }
            });
        }
    }
}


