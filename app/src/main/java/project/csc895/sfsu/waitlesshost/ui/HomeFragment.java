package project.csc895.sfsu.waitlesshost.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import project.csc895.sfsu.waitlesshost.model.Number;
import project.csc895.sfsu.waitlesshost.model.Table;


public class HomeFragment extends Fragment {

    private static final String TAG = "Home Fragment";
    private static final String ARGS_RESTAURANT_ID = "restaurantID";
    private static final String RESTAURANT_ID_CHILD = "restaurantID";
    private static final String NUMBER_CHILD = "numbers";
    private static final String TABLE_CHILD = "tables";
    private static final String TABLE_ID_CHILD = "tableID";
    private static final String TABLE_STATUS_OPEN = "Open";
    private static final String TABLE_STATUS_SEATED = "Seated";
    private static final String TABLE_STATUS_DIRTY = "Dirty";
    private static final String STATUS_CHILD = "status";
    private static final String NUMBER_ID_CHILD = "numberID";
    private static final String NUMBER_STATUS_COMPLETED = "Completed";
    public final static String EXTRA_TABLE_SIZE = "Pass Table size";
    public final static String EXTRA_TABLE_NAME = "Pass Table name";
    public final static String EXTRA_TABLE_ID = "Pass Table id";
    public final static String EXTRA_RESTAURANT_ID = "Pass Restaurant id";
    private String restaurantID;
    private RecyclerView openRecyclerView, seatedRecyclerView, dirtyRecyclerView;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Retrieve data from main activity
        Bundle args = getArguments();
        restaurantID = args.getString(ARGS_RESTAURANT_ID);
        Log.d("restaurantID", restaurantID);

        initViews(view);
        loadTableInfo();

        return view;
    }

    private void initViews(View view) {
        openRecyclerView = (RecyclerView) view.findViewById(R.id.open_list);
        seatedRecyclerView = (RecyclerView) view.findViewById(R.id.seated_list);
        dirtyRecyclerView = (RecyclerView) view.findViewById(R.id.dirty_list);

        openRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        seatedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dirtyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void loadTableInfo() {
        showList(TABLE_STATUS_OPEN, openRecyclerView);
        showList(TABLE_STATUS_SEATED, seatedRecyclerView);
        showList(TABLE_STATUS_DIRTY, dirtyRecyclerView);
    }

    private void showList(final String tableStatus, RecyclerView recyclerView) {
        Query query = mDatabase.child(TABLE_CHILD)
                .orderByChild(RESTAURANT_ID_CHILD)
                .equalTo(restaurantID);

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Table, TableViewHolder>(
                Table.class,
                R.layout.item_brief_table,
                TableViewHolder.class,
                query) {
            @Override
            protected void populateViewHolder(TableViewHolder viewHolder, Table table, int position) {
                String status = table.getStatus();
                if (status.equals(tableStatus)) {
                    viewHolder.setTableName(table.getTableName());
                    viewHolder.onClick(table);
                } else {
                    viewHolder.mTableNameLinearLayout.setVisibility(View.GONE);  // NEED. otherwise the separator line will get overlapped
                    viewHolder.mTableName.setVisibility(View.GONE);
                    viewHolder.mSeparator.setVisibility(View.GONE); // NEED. otherwise has extra space
                }
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder {
        private TextView mTableName;
        private LinearLayout mTableNameLinearLayout;
        private View mSeparator;

        public TableViewHolder(View itemView) {
            super(itemView);
            mTableName = (TextView) itemView.findViewById(R.id.tableName);
            mTableNameLinearLayout = (LinearLayout) itemView.findViewById(R.id.tableNameLinearLayout);
            mSeparator = itemView.findViewById(R.id.tableSeparator);
        }

        public void setTableName(String tableName) {
            mTableName.setText(tableName);
        }

        public void onClick(final Table table) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTableDetailsAlertDialog(table);
                }
            });
        }
    }

    private static void showTableDetailsAlertDialog(final Table table) {
        String tableStatus = table.getStatus();
        String numberID = table.getNumberID();

        LayoutInflater inflater = mActivity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alert_dialog_table, null);

        TextView tableNameField = alertLayout.findViewById(R.id.tableName);
        TextView tableSizeField = alertLayout.findViewById(R.id.tableSize);
        TextView tableStatusField = alertLayout.findViewById(R.id.tableStatus);

        String displayName = "Table " + table.getTableName();
        tableNameField.setText(displayName);
        tableSizeField.setText(String.valueOf(table.getTableSize()));
        tableStatusField.setText(tableStatus);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(alertLayout); // this is set the view from XML inside AlertDialog
        builder.setCancelable(false); // Disallow cancel of AlertDialog on click of back button and outside touch

        if (tableStatus.equals(TABLE_STATUS_OPEN)) {
            builder.setPositiveButton("Assign Table",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mActivity, AssignWaitlistActivity.class);
                            intent.putExtra(EXTRA_RESTAURANT_ID, table.getRestaurantID());
                            intent.putExtra(EXTRA_TABLE_SIZE, table.getTableSize());
                            intent.putExtra(EXTRA_TABLE_ID, table.getTableID());
                            intent.putExtra(EXTRA_TABLE_NAME, table.getTableName());
                            mActivity.startActivity(intent);
                        }
                    });

        } else if (tableStatus.equals(TABLE_STATUS_SEATED) && numberID != null) {
            loadNumberInfo(alertLayout, numberID);
            builder.setPositiveButton("Clean Table",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showConfirmCleanAlertDialog(table);
                        }
                    });
        } else if (tableStatus.equals(TABLE_STATUS_DIRTY)) {
            builder.setPositiveButton("Open Table",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showConfirmOpenAlertDialog(table);
                        }
                    });
        }

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // dismiss the alert dialog autoly
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private static void loadNumberInfo(View alertLayout, String numberID) {
        ImageView numberIcon = alertLayout.findViewById(R.id.numberIcon);
        ImageView customerIcon = alertLayout.findViewById(R.id.customerIcon);
        ImageView phoneIcon = alertLayout.findViewById(R.id.phoneIcon);
        ImageView partyIcon = alertLayout.findViewById(R.id.partyIcon);

        final TextView numberNameField = alertLayout.findViewById(R.id.numberName);
        final TextView customerNameField = alertLayout.findViewById(R.id.customerName);
        final TextView customerTelField = alertLayout.findViewById(R.id.customerTelephone);
        final TextView partyNumberField = alertLayout.findViewById(R.id.customerPartyNumber);

        numberIcon.setVisibility(View.VISIBLE);
        customerIcon.setVisibility(View.VISIBLE);
        phoneIcon.setVisibility(View.VISIBLE);
        partyIcon.setVisibility(View.VISIBLE);
        numberNameField.setVisibility(View.VISIBLE);
        customerNameField.setVisibility(View.VISIBLE);
        customerTelField.setVisibility(View.VISIBLE);
        partyNumberField.setVisibility(View.VISIBLE);

        //load customer info in Number child
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference numberRef = ref.child(NUMBER_CHILD).child(numberID);

        numberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Number number = dataSnapshot.getValue(Number.class);
                if (number != null) {
                    String numberName = "Number " + number.getNumberName();
                    numberNameField.setText(numberName);
                    customerNameField.setText(number.getUsername());
                    customerTelField.setText(number.getPhone());
                    partyNumberField.setText(String.valueOf(number.getPartyNumber()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void showConfirmCleanAlertDialog(final Table table) {
        String tableName = table.getTableName();
        String message = "Are you sure to clean the Table " + tableName + "?";

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(message);
        builder.setCancelable(false); // Disallow cancel of AlertDialog on click of back button and outside touch

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cleanTable(table);
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

    private static void cleanTable(Table table) {
        String tableID = table.getTableID();
        String numberID = table.getNumberID();

        updateTableStatus(tableID);
        updateNumberStatus(numberID);
        Toast.makeText(mActivity, "Table is under cleaning!", Toast.LENGTH_SHORT).show();
    }

    private static void updateTableStatus(String tableID) {
        //Table status: from Seated to Dirty
        //assign null to numberID child indicating no guest
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tableRef = databaseRef.child(TABLE_CHILD).child(tableID);
        tableRef.child(STATUS_CHILD).setValue(TABLE_STATUS_DIRTY);
        Log.d(TAG, "Table status change to Dirty");

        tableRef.child(NUMBER_ID_CHILD).setValue(null);
        Log.d(TAG, "Set numberID to null");
    }

    private static void updateNumberStatus(String numberID) {
        //Number status: from Dining to Completed
        //assign null to tableID child
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference numberRef = databaseRef.child(NUMBER_CHILD).child(numberID);
        numberRef.child(STATUS_CHILD).setValue(NUMBER_STATUS_COMPLETED);
        Log.d(TAG, "Number status change to Completed");

        numberRef.child(TABLE_ID_CHILD).setValue(null);
        Log.d(TAG, "Set tableID to null");
    }

    private static void showConfirmOpenAlertDialog(final Table table) {
        String tableName = table.getTableName();
        String message = "Are you sure to Open the Table " + tableName + "?";

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(message);
        builder.setCancelable(false); // Disallow cancel of AlertDialog on click of back button and outside touch

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openTable(table);
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

    private static void openTable(Table table) {
        String tableID = table.getTableID();

        //Update Table status: from Dirty to Open
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tableRef = databaseRef.child(TABLE_CHILD).child(tableID);
        tableRef.child(STATUS_CHILD).setValue(TABLE_STATUS_OPEN);
        Log.d(TAG, "Table status change to Open");
        Toast.makeText(mActivity, "Table Opened!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTableInfo();  // refresh if data changes
    }
}
