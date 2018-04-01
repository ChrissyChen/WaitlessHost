package project.csc895.sfsu.waitlesshost.ui;

/**
 * Created by Chrissy on 3/20/18.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import project.csc895.sfsu.waitlesshost.R;
import project.csc895.sfsu.waitlesshost.model.Message;
import project.csc895.sfsu.waitlesshost.model.Number;
import project.csc895.sfsu.waitlesshost.model.Table;
import project.csc895.sfsu.waitlesshost.model.User;
import project.csc895.sfsu.waitlesshost.model.Waitlist;

public class NumberDetailedActivity extends AppCompatActivity {

    private static final String TAG = "NumberDetailedActivity";
    private static final String NUMBER_CHILD = "numbers";
    private static final String WAITLIST_CHILD = "waitlists";
    private static final String TABLE_CHILD = "tables";
    private static final String TABLE_ID_CHILD = "tableID";
    private static final String NUMBER_ID_CHILD = "numberID";
    private static final String RESTAURANT_ID_CHILD = "restaurantID";
    private static final String STATUS_CHILD = "status";
    private static final String NUMBER_STATUS_WAITING = "Waiting";
    private static final String NUMBER_STATUS_DINING = "Dining";
    private static final String NUMBER_STATUS_CANCELLED = "Cancelled";
    private static final String NUMBER_STATUS_COMPLETED = "Completed";
    private static final String TABLE_STATUS_DIRTY = "Dirty";
    private static final String WAIT_NUM_TABLE_A_CHILD = "waitNumTableA";
    private static final String WAIT_NUM_TABLE_B_CHILD = "waitNumTableB";
    private static final String WAIT_NUM_TABLE_C_CHILD = "waitNumTableC";
    private static final String WAIT_NUM_TABLE_D_CHILD = "waitNumTableD";
    private static final String MESSAGE_CHILD = "messages";
    private static final String USER_CHILD = "users";
    private TextView numberNameField, statusField, customerName, customerPhone, partySize, createdTime;
    private Button completeButton, cancelButton, waitButton, notifyButton;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String numberID, restaurantID, waitlistID, tableID;
    private String numberName, restaurantName;
    private int waitNumTableA, waitNumTableB, waitNumTableC, waitNumTableD;
    private ImageView tableIcon;
    private TextView tableNameField;
    private String userID, userToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_detail);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        Intent intent = getIntent();
        numberID = intent.getStringExtra(GuestFragment.EXTRA_NUMBER_ID);
        restaurantID = intent.getStringExtra(GuestFragment.EXTRA_RESTAURANT_ID);
        userID = intent.getStringExtra(GuestFragment.EXTRA_USER_ID);
        Log.d(TAG, "user ID " + userID);

        initViews();
        loadNumberInfo();
        getWaitlistInfo();
        getUserToken();

    }

    private void initViews() {
        numberNameField = (TextView) findViewById(R.id.number);
        statusField = (TextView) findViewById(R.id.status);
        customerName = (TextView) findViewById(R.id.customerName);
        customerPhone = (TextView) findViewById(R.id.customerTelephone);
        partySize = (TextView) findViewById(R.id.customerPartySize);
        createdTime = (TextView) findViewById(R.id.numberCreatedTime);

        tableIcon = (ImageView) findViewById(R.id.tableIcon);
        tableNameField = (TextView) findViewById(R.id.tableName);

        completeButton = (Button) findViewById(R.id.completeButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        waitButton = (Button) findViewById(R.id.waitButton);
        notifyButton = (Button) findViewById(R.id.notifyButton);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmCompleteAlertDialog();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmCancelAlertDialog();
            }
        });
        waitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmWaitAlertDialog();
            }
        });
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmNotifyAlertDialog();
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
                    tableID = number.getTableID();  // get tableID
                    //userID = number.getUserID();  // get userID. get through intent
                    restaurantName = number.getRestaurantName();

                    numberName = number.getNumberName();
                    String displayNumberName = "Number " + numberName;
                    numberNameField.setText(displayNumberName);
                    statusField.setText(status);
                    customerName.setText(number.getUsername());
                    customerPhone.setText(number.getPhone());
                    partySize.setText(String.valueOf(number.getPartySize()));
                    createdTime.setText(number.getTimeCreated());

                    if (status.equals(NUMBER_STATUS_WAITING)) {  //show cancel and notify buttons. hide other buttons
                        if (userID.equals("N/A")) {     // if userID is "N/A" means the number is created by restaurant
                            cancelButton.setVisibility(View.VISIBLE);
                            notifyButton.setVisibility(View.GONE);
                        } else {
                            cancelButton.setVisibility(View.VISIBLE);
                            notifyButton.setVisibility(View.VISIBLE);
                        }
                        completeButton.setVisibility(View.GONE);
                        waitButton.setVisibility(View.GONE);
                    } else if (status.equals(NUMBER_STATUS_DINING)) {   // show complete button. hide other buttons
                        completeButton.setVisibility(View.VISIBLE);
                        cancelButton.setVisibility(View.GONE);
                        waitButton.setVisibility(View.GONE);
                        notifyButton.setVisibility(View.GONE);
                    } else if (status.equals(NUMBER_STATUS_CANCELLED)) { // show wait button. hide other buttons
                        waitButton.setVisibility(View.VISIBLE);
                        completeButton.setVisibility(View.GONE);
                        cancelButton.setVisibility(View.GONE);
                        notifyButton.setVisibility(View.GONE);
                    } else if (status.equals(NUMBER_STATUS_COMPLETED)) {   // hide all buttons
                        completeButton.setVisibility(View.GONE);
                        cancelButton.setVisibility(View.GONE);
                        waitButton.setVisibility(View.GONE);
                        notifyButton.setVisibility(View.GONE);
                    }

                    if (tableID != null) {
                        tableIcon.setVisibility(View.VISIBLE);
                        tableNameField.setVisibility(View.VISIBLE);
                        loadTableName();
                    } else {
                        tableIcon.setVisibility(View.GONE);
                        tableNameField.setVisibility(View.GONE);
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

    /* Cancel a number */
    private void showConfirmCancelAlertDialog() {
        String message = "Add the number to the Cancel list?";

        AlertDialog.Builder builder = new AlertDialog.Builder(NumberDetailedActivity.this);
        builder.setMessage(message);
        builder.setCancelable(false); // Disallow cancel of AlertDialog on click of back button and outside touch

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelNumber();
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

    private void cancelNumber() {
        //cancel number: set number status to cancelled. update db. update textview ui (auto)
        //waitNumTable -1
        //toast
        DatabaseReference numberRef = mDatabase.child(NUMBER_CHILD).child(numberID);
        numberRef.child(STATUS_CHILD).setValue(NUMBER_STATUS_CANCELLED);

        updateWaitlistInfoWhenCancel();
        Toast.makeText(NumberDetailedActivity.this, getString(R.string.cancel_succeed), Toast.LENGTH_SHORT).show();
    }

    private void updateWaitlistInfoWhenCancel() {
        DatabaseReference ref = mDatabase.child(WAITLIST_CHILD).child(waitlistID);
        if (numberName.charAt(2) == 'A') {
            waitNumTableA -= 1;
            ref.child(WAIT_NUM_TABLE_A_CHILD).setValue(waitNumTableA);
        } else if (numberName.charAt(2) == 'B') {
            waitNumTableB -= 1;
            ref.child(WAIT_NUM_TABLE_B_CHILD).setValue(waitNumTableB);
        } else if (numberName.charAt(2) == 'C') {
            waitNumTableC -= 1;
            ref.child(WAIT_NUM_TABLE_C_CHILD).setValue(waitNumTableC);
        } else if (numberName.charAt(2) == 'D') {
            waitNumTableD -= 1;
            ref.child(WAIT_NUM_TABLE_D_CHILD).setValue(waitNumTableD);
        }
    }

    /* Add a number back to the waiting list */
    private void showConfirmWaitAlertDialog() {
        String message = "Add the number back to the Waiting list?";

        AlertDialog.Builder builder = new AlertDialog.Builder(NumberDetailedActivity.this);
        builder.setMessage(message);
        builder.setCancelable(false); // Disallow cancel of AlertDialog on click of back button and outside touch

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNumberBackToWaitingList();
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

    private void addNumberBackToWaitingList() {
        //set number status to waiting. update db. update textview ui (auto)
        //waitNumTable +1
        //toast
        DatabaseReference numberRef = mDatabase.child(NUMBER_CHILD).child(numberID);
        numberRef.child(STATUS_CHILD).setValue(NUMBER_STATUS_WAITING);

        updateWaitlistInfoWhenWait();
        Toast.makeText(NumberDetailedActivity.this, getString(R.string.wait_succeed), Toast.LENGTH_LONG).show();
    }

    private void updateWaitlistInfoWhenWait() {
        DatabaseReference ref = mDatabase.child(WAITLIST_CHILD).child(waitlistID);
        if (numberName.charAt(2) == 'A') {
            waitNumTableA += 1;
            ref.child(WAIT_NUM_TABLE_A_CHILD).setValue(waitNumTableA);
        } else if (numberName.charAt(2) == 'B') {
            waitNumTableB += 1;
            ref.child(WAIT_NUM_TABLE_B_CHILD).setValue(waitNumTableB);
        } else if (numberName.charAt(2) == 'C') {
            waitNumTableC += 1;
            ref.child(WAIT_NUM_TABLE_C_CHILD).setValue(waitNumTableC);
        } else if (numberName.charAt(2) == 'D') {
            waitNumTableD += 1;
            ref.child(WAIT_NUM_TABLE_D_CHILD).setValue(waitNumTableD);
        }
    }

    /* show more info when number is dining status */
    private void loadTableName() {
        DatabaseReference ref = mDatabase.child(TABLE_CHILD).child(tableID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Table table = dataSnapshot.getValue(Table.class);
                if (table != null) {
                    String displayTableName = "Table " + table.getTableName();
                    tableNameField.setText(displayTableName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* Complete a number */
    private void showConfirmCompleteAlertDialog() {
        String message = "Add the number to Completed list?";

        AlertDialog.Builder builder = new AlertDialog.Builder(NumberDetailedActivity.this);
        builder.setMessage(message);
        builder.setCancelable(false); // Disallow cancel of AlertDialog on click of back button and outside touch

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        completeNumber();
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

    private void completeNumber() {
        //Update Table status: from Seated to Dirty
        //set table's numberID to null
        DatabaseReference tableRef = mDatabase.child(TABLE_CHILD).child(tableID);
        tableRef.child(STATUS_CHILD).setValue(TABLE_STATUS_DIRTY);
        tableRef.child(NUMBER_ID_CHILD).setValue(null);

        //Update Number status: from Dining to Completed
        //set number's tableID to null
        DatabaseReference numberRef = mDatabase.child(NUMBER_CHILD).child(numberID);
        numberRef.child(STATUS_CHILD).setValue(NUMBER_STATUS_COMPLETED);
        numberRef.child(TABLE_ID_CHILD).setValue(null);

        Toast.makeText(NumberDetailedActivity.this, "Number Completed and Table is under cleaning!", Toast.LENGTH_LONG).show();
    }

    /* Notify a number */
    private void showConfirmNotifyAlertDialog() {
        String message = "Send a notification to the guest that the table will be ready soon?";

        AlertDialog.Builder builder = new AlertDialog.Builder(NumberDetailedActivity.this);
        builder.setMessage(message);
        builder.setCancelable(false); // Disallow cancel of AlertDialog on click of back button and outside touch

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendTableReadyNotification();
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

    private void sendTableReadyNotification() {
        // add a new node in message child. it will trigger FCM Cloud Function to push a notification
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(MESSAGE_CHILD);
        String key = ref.push().getKey();  // newly generated messageID

        Log.d(TAG, "user token " + userToken);
        if (userToken != null) {
            String title = "Waitlist - " + restaurantName;
            String message = "Your Number [" + numberName + "] will be assign to a table soon!";
            Message newMessage = new Message(restaurantID, userToken, title, message);
            ref.child(key).setValue(newMessage);
            Log.d(TAG, "creating a new message succeed!");
            Toast.makeText(NumberDetailedActivity.this, "Notification Sent!", Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "user token is null");
        }
    }

    private void getUserToken() {

        DatabaseReference ref = mDatabase.child(USER_CHILD).child(userID);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    userToken = user.getTokenFCM();
                    Log.d(TAG, "user token inside callback " + userToken);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

