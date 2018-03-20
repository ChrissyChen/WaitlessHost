package project.csc895.sfsu.waitlesshost.ui;

/**
 * Created by Chrissy on 3/20/18.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import project.csc895.sfsu.waitlesshost.R;
import project.csc895.sfsu.waitlesshost.model.Number;
import project.csc895.sfsu.waitlesshost.model.Waitlist;

public class NumberDetailedActivity extends AppCompatActivity {

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
    private LinearLayout mLinearLayout;
    private TextView numberNameField, statusField, customerName, customerPhone, partyNumber, createdTime;
    private Button completeButton, cancelButton, waitButton;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String numberID, restaurantID, waitlistID;
    private String numberName;
    private int waitNumTableA, waitNumTableB, waitNumTableC, waitNumTableD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_detail);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        Intent intent = getIntent();
        numberID = intent.getStringExtra(GuestFragment.EXTRA_NUMBER_ID);
        restaurantID = intent.getStringExtra(GuestFragment.EXTRA_RESTAURANT_ID);

        initViews();
        loadNumberInfo();
        getWaitlistInfo();

    }

    private void initViews() {
        mLinearLayout = (LinearLayout) findViewById(R.id.numberDetailLinearLayout);
        numberNameField = (TextView) findViewById(R.id.number);
        statusField = (TextView) findViewById(R.id.status);
        customerName = (TextView) findViewById(R.id.customerName);
        customerPhone = (TextView) findViewById(R.id.customerTelephone);
        partyNumber = (TextView) findViewById(R.id.customerPartyNumber);
        createdTime = (TextView) findViewById(R.id.numberCreatedTime);

        completeButton = (Button) findViewById(R.id.completeButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        waitButton = (Button) findViewById(R.id.waitButton);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO popup window and trigger table to be available
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // popup window and trigger cancel the number
                showCancelPopupWindow();
            }
        });
        waitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo popup window and trigger change status to waiting, numWait +1
                showWaitPopupWindow();
            }
        });
    }

    private void loadNumberInfo() {
        DatabaseReference ref = mDatabase.child(NUMBER_CHILD).child(numberID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Number number = dataSnapshot.getValue(Number.class);
                if (number != null) {
                    String status = number.getStatus();
                    numberName = number.getNumberName();

                    numberNameField.setText(numberName);
                    statusField.setText(status);
                    customerName.setText(number.getUsername());
                    customerPhone.setText(number.getPhone());
                    partyNumber.setText(String.valueOf(number.getPartyNumber()));
                    createdTime.setText(number.getTimeCreated());

                    if (status.equals(STATUS_WAITING)) {         //show cancel button. hide other buttons
                        cancelButton.setVisibility(View.VISIBLE);
                        completeButton.setVisibility(View.GONE);
                        waitButton.setVisibility(View.GONE);
                    } else if (status.equals(STATUS_DINING)) {   // show complete button. hide other buttons
                        completeButton.setVisibility(View.VISIBLE);
                        cancelButton.setVisibility(View.GONE);
                        waitButton.setVisibility(View.GONE);
                    } else if (status.equals(STATUS_CANCELLED)) { // show wait button. hide other buttons
                        waitButton.setVisibility(View.VISIBLE);
                        completeButton.setVisibility(View.GONE);
                        cancelButton.setVisibility(View.GONE);
                    } else if (status.equals(STATUS_COMPLETED)) {   // hide all buttons
                        completeButton.setVisibility(View.GONE);
                        cancelButton.setVisibility(View.GONE);
                        waitButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                    for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
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

    /* Cancel a number */
    private void showCancelPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) NumberDetailedActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View customView = inflater.inflate(R.layout.popup_window_cancel, null);
            final PopupWindow popupWindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(mLinearLayout, Gravity.CENTER, 0, 0);

            Button yesButton = (Button) customView.findViewById(R.id.yesButton);
            Button noButton = (Button) customView.findViewById(R.id.noButton);
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelNumber();
                    popupWindow.dismiss();

                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
    }

    private void cancelNumber() {
        //cancel number: set number status to cancelled. update db. update textview ui (auto)
        //waitNumTable -1
        //toast
        DatabaseReference numberRef = mDatabase.child(NUMBER_CHILD).child(numberID);
        numberRef.child(STATUS_CHILD).setValue(STATUS_CANCELLED);

        updateWaitlistInfoWhenCancel();
        Toast.makeText(NumberDetailedActivity.this, getString(R.string.cancel_succeed), Toast.LENGTH_SHORT).show();
    }

    private void updateWaitlistInfoWhenCancel() {
        DatabaseReference ref = mDatabase.child(WAITLIST_CHILD).child(waitlistID);
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


    /* Add a number back to the waiting list */
    private void showWaitPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) NumberDetailedActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View customView = inflater.inflate(R.layout.popup_window_wait, null);
            final PopupWindow popupWindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(mLinearLayout, Gravity.CENTER, 0, 0);

            Button yesButton = (Button) customView.findViewById(R.id.yesButton);
            Button noButton = (Button) customView.findViewById(R.id.noButton);
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNumberBackToWaitingList();
                    popupWindow.dismiss();

                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
    }

    private void addNumberBackToWaitingList() {
        //set number status to waiting. update db. update textview ui (auto)
        //waitNumTable +1
        //toast
        DatabaseReference numberRef = mDatabase.child(NUMBER_CHILD).child(numberID);
        numberRef.child(STATUS_CHILD).setValue(STATUS_WAITING);

        updateWaitlistInfoWhenWait();
        Toast.makeText(NumberDetailedActivity.this, getString(R.string.wait_succeed), Toast.LENGTH_LONG).show();
    }

    private void updateWaitlistInfoWhenWait() {
        DatabaseReference ref = mDatabase.child(WAITLIST_CHILD).child(waitlistID);
        if (numberName.charAt(0) == 'A') {
            waitNumTableA += 1;
            ref.child(WAIT_NUM_TABLE_A_CHILD).setValue(waitNumTableA);
        } else if (numberName.charAt(0) == 'B') {
            waitNumTableB += 1;
            ref.child(WAIT_NUM_TABLE_B_CHILD).setValue(waitNumTableB);
        } else if (numberName.charAt(0) == 'C') {
            waitNumTableC += 1;
            ref.child(WAIT_NUM_TABLE_C_CHILD).setValue(waitNumTableC);
        } else if (numberName.charAt(0) == 'D') {
            waitNumTableD += 1;
            ref.child(WAIT_NUM_TABLE_D_CHILD).setValue(waitNumTableD);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.top_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.homeAction:
//                startActivity(new Intent(NumberDetailedActivity.this, MainActivity.class));
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}

