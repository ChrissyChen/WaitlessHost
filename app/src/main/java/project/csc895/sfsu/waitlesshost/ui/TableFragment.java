package project.csc895.sfsu.waitlesshost.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import project.csc895.sfsu.waitlesshost.R;
import project.csc895.sfsu.waitlesshost.model.RestaurantTable;
import project.csc895.sfsu.waitlesshost.model.Table;


public class TableFragment extends Fragment {

    private static final String TAG = "Table Fragment";
    private static final String ARGS_RESTAURANT_ID = "restaurantID";
    private static final String TABLE_CHILD = "tables";
    private static final String RESTAURANT_TABLE_CHILD = "restaurantTables";
    private static final String RESTAURANT_ID_CHILD = "restaurantID";
    private static final String NUM_TABLE_A_CHILD = "numTableA";
    private static final String NUM_TABLE_B_CHILD = "numTableB";
    private static final String NUM_TABLE_C_CHILD = "numTableC";
    private static final String NUM_TABLE_D_CHILD = "numTableD";
    private static final String LIST_TABLE_A_CHILD = "listTableA";
    private static final String LIST_TABLE_B_CHILD = "listTableB";
    private static final String LIST_TABLE_C_CHILD = "listTableC";
    private static final String LIST_TABLE_D_CHILD = "listTableD";
    private static final int SIZE_TABLE_A = 2;
    private static final int SIZE_TABLE_B = 4;
    private static final int SIZE_TABLE_C = 6;
    private static final int SIZE_TABLE_D = 10;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String restaurantID, restaurantTableID;
    private EditText inputTableA, inputTableB, inputTableC, inputTableD;
    private int prevNumTableA, prevNumTableB, prevNumTableC, prevNumTableD;
    private ArrayList<String> listTableA, listTableB, listTableC, listTableD;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_table, container, false);

        // Retrieve data from main activity
        Bundle args = getArguments();
        restaurantID = args.getString(ARGS_RESTAURANT_ID);

        initTextViews(view);
        loadTablesWithRestaurantID();   // retrieve data from database

        Button btnSave = (Button) view.findViewById(R.id.save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTablesToDatabase();
            }
        });

        return view;
    }

    private void initTextViews(View view) {
        inputTableA = (EditText) view.findViewById(R.id.num_tableA);
        inputTableB = (EditText) view.findViewById(R.id.num_tableB);
        inputTableC = (EditText) view.findViewById(R.id.num_tableC);
        inputTableD = (EditText) view.findViewById(R.id.num_tableD);
    }

    private void loadTablesWithRestaurantID() {
        Query query = mDatabase.child(RESTAURANT_TABLE_CHILD)
                .orderByChild(RESTAURANT_ID_CHILD)
                .equalTo(restaurantID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    Log.d(TAG, "NO RESTAURANT TABLE FOUND!");
                } else {
                    for (DataSnapshot objSnapshot: dataSnapshot.getChildren()) {
                        RestaurantTable restaurantTable = objSnapshot.getValue(RestaurantTable.class);
                        if (restaurantTable != null) {
                            restaurantTableID = restaurantTable.getRestaurantTableID();
                            //Log.d("num table a: ", String.valueOf(restaurantTable.getNumTableA()));
                            getDatabaseNum(restaurantTable);
                            setDatabaseNumToEditText();
                            getListTable(restaurantTable);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDatabaseNum(RestaurantTable restaurantTable) {
        prevNumTableA = restaurantTable.getNumTableA();
        prevNumTableB = restaurantTable.getNumTableB();
        prevNumTableC = restaurantTable.getNumTableC();
        prevNumTableD = restaurantTable.getNumTableD();
    }

    private void setDatabaseNumToEditText() {
        inputTableA.setText(String.valueOf(prevNumTableA));
        inputTableB.setText(String.valueOf(prevNumTableB));
        inputTableC.setText(String.valueOf(prevNumTableC));
        inputTableD.setText(String.valueOf(prevNumTableD));
    }

    private void getListTable(RestaurantTable restaurantTable) {
        listTableA = restaurantTable.getListTableA();
        listTableB = restaurantTable.getListTableB();
        listTableC = restaurantTable.getListTableC();
        listTableD = restaurantTable.getListTableD();

        if (listTableA == null) {
            listTableA = new ArrayList<>();
        }
        if (listTableB == null) {
            listTableB = new ArrayList<>();
        }
        if (listTableC == null) {
            listTableC = new ArrayList<>();
        }
        if (listTableD == null) {
            listTableD = new ArrayList<>();
        }
    }


    private void saveTablesToDatabase() {
        DatabaseReference restaurantTableRef = mDatabase.child(RESTAURANT_TABLE_CHILD).child(restaurantTableID);
        DatabaseReference tableRef = mDatabase.child(TABLE_CHILD);

        String numTableA = inputTableA.getText().toString().trim();
        String numTableB = inputTableB.getText().toString().trim();
        String numTableC = inputTableC.getText().toString().trim();
        String numTableD = inputTableD.getText().toString().trim();

        if (TextUtils.isEmpty(numTableA)) {
            inputTableA.requestFocus();
            inputTableA.setError(getString(R.string.empty_number));
            return;
        }
        if (TextUtils.isEmpty(numTableB)) {
            inputTableB.requestFocus();
            inputTableB.setError(getString(R.string.empty_number));
            return;
        }
        if (TextUtils.isEmpty(numTableC)) {
            inputTableC.requestFocus();
            inputTableC.setError(getString(R.string.empty_number));
            return;
        }
        if (TextUtils.isEmpty(numTableD)) {
            inputTableD.requestFocus();
            inputTableD.setError(getString(R.string.empty_number));
            return;
        }

        int updatedNumTableA = Integer.valueOf(numTableA);
        int updatedNumTableB = Integer.valueOf(numTableB);
        int updatedNumTableC = Integer.valueOf(numTableC);
        int updatedNumTableD = Integer.valueOf(numTableD);

        if (updatedNumTableA > 20) {
            inputTableA.requestFocus();
            inputTableA.setError(getString(R.string.big_number_error));
            return;
        }
        if (updatedNumTableB > 20) {
            inputTableB.requestFocus();
            inputTableB.setError(getString(R.string.big_number_error));
            return;
        }
        if (updatedNumTableC > 20) {
            inputTableC.requestFocus();
            inputTableC.setError(getString(R.string.big_number_error));
            return;
        }
        if (updatedNumTableD > 20) {
            inputTableD.requestFocus();
            inputTableD.setError(getString(R.string.big_number_error));
            return;
        }

        updateDB(prevNumTableA, updatedNumTableA, listTableA, LIST_TABLE_A_CHILD, SIZE_TABLE_A, "A", NUM_TABLE_A_CHILD, restaurantTableRef, tableRef);
        updateDB(prevNumTableB, updatedNumTableB, listTableB, LIST_TABLE_B_CHILD, SIZE_TABLE_B, "B", NUM_TABLE_B_CHILD, restaurantTableRef, tableRef);
        updateDB(prevNumTableC, updatedNumTableC, listTableC, LIST_TABLE_C_CHILD, SIZE_TABLE_C, "C", NUM_TABLE_C_CHILD, restaurantTableRef, tableRef);
        updateDB(prevNumTableD, updatedNumTableD, listTableD, LIST_TABLE_D_CHILD, SIZE_TABLE_D, "D", NUM_TABLE_D_CHILD, restaurantTableRef, tableRef);

        Toast.makeText(getActivity(), "Table Numbers Saved! ", Toast.LENGTH_SHORT).show();
    }

    private void updateDB(int prevNum, int curNum, ArrayList<String> listTable, final String listTableChild, final int sizeTable,
                          String type, final String numTableChild, DatabaseReference restaurantTableRef, DatabaseReference tableRef) {

        if (prevNum == curNum) {           // do nothing. no need to update db
            return;

        } else if (prevNum > curNum) {     // remove node diff
            // remove last diff number of tableID in listTable of restaurantTable child and remove corresponding tables in table child
            int diff = prevNum - curNum;
            for (int i = 0; i < diff; i++) {
                String tableID = listTable.get(curNum);
                tableRef.child(tableID).removeValue();

                listTable.remove(curNum);
            }

        } else if (prevNum < curNum) {     // add node diff
            for (int i = prevNum + 1; i <= curNum; i++) {
                // create a new table
                String key = tableRef.push().getKey();  // newly generated tableID
                Table newTable = new Table(key, restaurantID, sizeTable, type + i, false, null);
                tableRef.child(key).setValue(newTable);
                // add newly created tableID to listTable
                listTable.add(key);
            }
        }

        restaurantTableRef.child(listTableChild).setValue(listTable);
        restaurantTableRef.child(numTableChild).setValue(curNum);
    }

}

